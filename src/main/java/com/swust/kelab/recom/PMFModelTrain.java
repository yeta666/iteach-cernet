package com.swust.kelab.recom;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;

import com.google.common.collect.Maps;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.UserModel;
/**
 * 基于概率矩阵分解建立评分预测
 * @author pjt
 *
 */
public class PMFModelTrain {
	final Logger logger = LoggerFactory.getLogger(getClass());
	private RecomDao recomDao= null;
	
	private int featureCount=4;
	private double regularizer = 0.001;
	private double learningRate =50;
	private boolean showProgress = true;
	private int maxIter = 200;
	
	public int pageSize=1000;
	
	private Matrix userFeatures;
	private Matrix itemFeatures;
	public int userCount;
	public int userCountPage=0;
	private int itemCount;

	private int totalPage;
	private int rateCount;
	private double meanRating;
	protected double offset;
	
	public volatile boolean isTrainFinish = false;
	//id與index不一致時快查表
	private Map<Integer,Integer> userIndexMap = Maps.newHashMapWithExpectedSize(1000);
	private Map<Integer,Integer> itemIndexMap = Maps.newHashMapWithExpectedSize(1000);
	
	//index與id不一致時快查表
	//private Map<Integer,Integer> userIdMap = Maps.newHashMapWithExpectedSize(1000);
	private Map<Integer,Integer> itemIdMap = Maps.newHashMapWithExpectedSize(1000);
		
	private PMFModelTrain(){}
	public PMFModelTrain(RecomDao recomDao,int featureCount,double regularizer,int maxIt){
		this.recomDao = recomDao;
		this.featureCount = featureCount;
		this.regularizer = regularizer;
		this.maxIter = maxIt;
	}
	/**
	 * 建立id索引index快查表,
	 * 只存储id与index不一致的情况，节约内存
	 */
	private void buildIndexMap(){
		if(userCount<=0||itemCount<=0){
			return ;
		}
		//批量处理userId
		int tp = 1;
		if(userCount>pageSize){
			tp = userCount%pageSize==0?userCount/pageSize:userCount/pageSize+1;
		}
		userCountPage= tp;
		
		
		int index = 1;
		for (int page = 1; page <= tp; page++) {
			List<Integer> users = recomDao.getUsersByBatch(page, pageSize);
			for (int i = 0; i < users.size();index++, i++) {
				if(users.get(i)!=index){
					userIndexMap.put(users.get(i), index);
					//userIdMap.put(index, users.get(i).getId());
				}
			}
		}
		//批量处理ItemId
		tp = 1;
		if(itemCount>pageSize){
			tp = itemCount%pageSize==0?itemCount/pageSize:itemCount/pageSize+1;
		}
		index = 1;
		for (int page = 1; page <= tp; page++) {
			List<Integer> items = recomDao.getCoursesByBatch(page, pageSize);
			for (int i = 0; i < items.size();index++, i++) {
				if(items.get(i)!=index){
					itemIndexMap.put(items.get(i), index);
					itemIdMap.put(index, items.get(i));
				}
			}
		}
	}
	
	private int userId2Index(int userId){
		Integer index = userIndexMap.get(userId);
		if(index!=null){
			return index;
		}
		return userId;
	}
	private int itemId2Index(int itemId){
		Integer index = itemIndexMap.get(itemId);
		if(index!=null){
			return index;
		}
		return itemId;
	}
	private int itemIndex2Id(int itemIndex){
		Integer id = itemIdMap.get(itemIndex);
		if(id!=null){
			return id;
		}
		return itemIndex;
	}
	//批量载入训练集合
	private List<UserRate> loadTrainSetByPage(int currentPage,int pageSize){
		return recomDao.getUserRateByBatch(currentPage, pageSize);
	}
	
	private void intialData(){
		userCount = recomDao.getUserCount();
		itemCount = recomDao.getCourseCount();
		rateCount = recomDao.getRateSize();
		meanRating = recomDao.getMeanRate();
		System.out.println("userCount="+userCount+",itemCount="+itemCount
				+",rateCount="+rateCount+",meanRate="+meanRating);
		this.offset = meanRating;
		if(rateCount>pageSize){
			totalPage = rateCount%pageSize==0?rateCount/pageSize:rateCount/pageSize+1;
		}else{
			totalPage = 1;
		}
		
	}
	/**
	 * 初始化特徵矩陣
	 */
	private void intialFeatrueVector(){
		userFeatures = DenseMatrix.Factory.zeros(userCount+1, featureCount);
		itemFeatures = DenseMatrix.Factory.zeros(featureCount,itemCount+1);
		
		// Initialize with random values:
		for (int f = 0; f < featureCount; f++) {
			for (int u = 1; u <= userCount; u++) {
				//MathUtil.nextGaussian();
				userFeatures.setAsDouble(0.1 * Distribution.normalRandom(0, 1),u, f);
			}
			for (int i = 1; i <= itemCount; i++) {
				itemFeatures.setAsDouble(0.1 * Distribution.normalRandom(0, 1),f, i);
			}
		}
	}
	
	public void bulidModel(){
		isTrainFinish = false;
		this.intialData();
		this.buildIndexMap();
		this.intialFeatrueVector();
		long s = System.currentTimeMillis();
		this.train();
		long e = System.currentTimeMillis();
		logger.debug("训练完成+"+(e-s)+"ms");
		System.out.println("模型訓練完成，耗時:"+(e-s)+"ms");
		isTrainFinish = true;
		//test
		try {
			System.out.println(this.recomItemIds(80401, 10));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void train(){
		int round = 0;
		double prevErr = 99999;
		double currErr = 9999;

		// Iteration:
		while (prevErr > currErr && round < maxIter) {
			double errSum = 0.0;
			Matrix userDelta = DenseMatrix.Factory.zeros(userCount+1,featureCount);
			Matrix itemDelta = DenseMatrix.Factory.zeros(featureCount,itemCount+1);
			
			for (int currentPage = 1; currentPage <= totalPage; currentPage++) {
				List<UserRate> trainData= this.loadTrainSetByPage(currentPage, pageSize);
				for (UserRate userRate: trainData) {
					double realRating = userRate.getRate()- meanRating;
					int userIndex = this.userId2Index(userRate.getUser());
					int itemIndex = this.itemId2Index(userRate.getCourse());
					Matrix userRow = userFeatures.selectRows(Ret.LINK, userIndex);
					Matrix itemCol = itemFeatures.selectColumns(Ret.LINK,itemIndex);
					double prediction =userRow.mtimes(Ret.NEW, true, 
							itemCol).getAsDouble(0,0);
					double userRowSum = userRow.getValueSum();
					double itemColSum = itemCol.getValueSum();
					double err = Math.pow(prediction - realRating, 2) + 0.5 * regularizer * (Math.pow(userRowSum, 2) + Math.pow(itemColSum, 2));
					errSum += err;
					
					// Compute gradients:
					double repmatValue = 2 * (prediction - realRating);
					for (int f = 0; f < featureCount; f++) {
						double tmpU = userFeatures.getAsDouble(userIndex, f);
						double tmpI = itemFeatures.getAsDouble(f, itemIndex);
						double Ix_p = repmatValue * tmpI + regularizer * tmpU;
						double Ix_m = repmatValue * tmpU + regularizer * tmpI;
						
						userDelta.setAsDouble(userDelta.getAsDouble(userIndex, f) + Ix_p,userIndex, f);
						itemDelta.setAsDouble(itemDelta.getAsDouble(f, itemIndex) + Ix_m,f, itemIndex);
					}
				}
			}
			
			// Update user and item features:
			//userFeaturesInc = userFeaturesInc.scale(momentum).plus(userDelta.scale(learningRate / rateCount));
			//userFeatures = userFeatures.plus(userFeaturesInc).scale(-1));
					
			// Update user and item features:
			double tmp = learningRate / rateCount;
			
			userFeatures = userFeatures.plus(userDelta.times(tmp)).times(-1);
			itemFeatures = itemFeatures.plus(itemDelta.times(tmp)).times(-1);
		    
		    round++;
		    // show progress:
		    prevErr = currErr;
		    currErr = errSum/rateCount;
		    
		    if (showProgress)
		    	logger.debug("迭代次数:"+round + "\t errSum="+errSum+"; prevErr="+prevErr+",currErr=" + currErr+",curr-prevr="+(Math.abs(prevErr - currErr)));

		}
	}
	
	private double predictValue(int userIndex,int itemIndex){
		Matrix userRow = userFeatures.selectRows(Ret.LINK, userIndex);
		Matrix itemCol = itemFeatures.selectColumns(Ret.LINK,itemIndex);
		double prediction =this.offset+userRow.mtimes(Ret.NEW, true, 
				itemCol).getAsDouble(0,0);
		return prediction;
	}
	
	
	public String recomItemIds(int userId,int topK) throws Exception{
		if(!isTrainFinish){
			throw new Exception("build model has not finish! please wait a letter");
		}
		int userIndex = this.userId2Index(userId);
		List<Integer> hasRateItems = recomDao.getUserRateItemIds(userId);
		BitSet ratedRateSet = new BitSet(100000);//
		for (int i = 0; i < hasRateItems.size(); i++) {
			ratedRateSet.set(this.itemId2Index(hasRateItems.get(i)));
		}
		
		FixSizedPriorityQueue<Entriy> recomQueue = new FixSizedPriorityQueue<Entriy>(topK);
		for (int item = 1; item <= itemCount; item++) {
			if(ratedRateSet.get(item)){
				continue;
			}
			double prediction = this.predictValue(userIndex, item);
			recomQueue.add(new Entriy(this.itemIndex2Id(item),prediction));
		}
		
		Object[] array = recomQueue.getQueue().toArray();
		StringBuffer result= new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			Entriy t = (Entriy)array[i];
			if(i<array.length-1){
				result.append(t.id).append(",");
			}else{
				result.append(t.id);
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		Matrix m1 = DenseMatrix.Factory.rand(2,2);
		Matrix m2 = DenseMatrix.Factory.rand(2,2);
		System.out.println(m1+""+m2+"=====");
		m2.plus(Ret.LINK,true, m1);
		System.out.println(m2);
		//m1.plus(m2).times(-1);
		//System.out.println(m1.plus(m2).times(-1));
		//System.out.println(m1);
		FixSizedPriorityQueue<Integer> queue = new FixSizedPriorityQueue<Integer>(5);
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			int t = r.nextInt(20);
			System.out.print(t+" ");
			queue.add(t);
		}
		
		System.out.println();
		System.out.println(queue.getQueue());
	}
	
}

//自己实现一个固定大小的优先队列，方便排序取最大的K个元素
	class FixSizedPriorityQueue<E extends Comparable>{
		private PriorityQueue<E> queue;
		private int maxSize; // 堆的最大容量
		public FixSizedPriorityQueue(int maxSize){
			this.maxSize = maxSize;
			queue = new PriorityQueue<E>(maxSize);
		}
		public void add(E e) {
	        if (queue.size() < maxSize) { // 未达到最大容量，直接添加
	            queue.add(e);
	        } else { // 队列已满
	            E peek = queue.peek();
	            if (e.compareTo(peek) > 0) { // 将新元素与当前堆顶元素比较，保留较大的元素
	                queue.poll();
	                queue.add(e);
	            }
	        }
	    }
		public Queue getQueue(){
			return this.queue;
		}
	}
	class Entriy implements Comparable<Entriy>{
		int id;
		double sim;
		public Entriy(int id,double sim){
			this.id = id;
			this.sim = sim;
		}
		@Override
		public int compareTo(Entriy o) {
			if(this.sim<o.sim){
				return -1;
			}
			return 1;
		}
		
	}
