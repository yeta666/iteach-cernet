package com.swust.kelab.recom;
import java.util.Random;

/**
 * This class implements several statistical distributions.
 * Note that we use UJMP package (http://www.ujmp.org) to implement this class.
 * Each distribution provides random sampling methods.
 * 
 * @author Joonseok Lee
 * @since 2012. 4. 20
 * @version 1.1
 */
public class Distribution {
	/*========================================
	 * Normal Distribution
	 *========================================*/
	/**
	 * Randomly sample 1 point from Normal Distribution with the given mean and standard deviation. 
	 * </br>mean+ͨ��Random����������̬�ֲ�&��˹�ֲ��������&*std
	 * @param ������mean Mean of this Normal Distribution.
	 * @param ��׼�� std Standard deviation of this Normal Distribution.
	 * @return The sample randomly drawn from the given distribution.
	 */
	public static double normalRandom(double mean, double std) {
		Random r = new Random();
		return mean + r.nextGaussian() * std;
	}
	
	/**
	 * Randomly sample several points from Normal Distribution with the given mean and standard deviation. 
	 * 
	 * @param mean Mean of this Normal Distribution.
	 * @param std Standard deviation of this Normal Distribution.
	 * @param count The number of samples to draw.
	 * @return The sample randomly drawn from the given distribution.
	 */
	public static double[] normalDistribution(double mean, double std, int count) {
		double[] result = new double[count];
		Random r = new Random();
		
		for (int i = 0; i < count; i++) {
			result[i] = mean + r.nextGaussian() * std;
		}
		
		return result;
	}
	
	/*========================================
	 * Gamma Distribution
	 *========================================*/
	/**
	 * Randomly sample 1 point from Gamma Distribution with the given parameters.
	 * We use the code from Mahout (http://mahout.apache.org/), available under Apache 2 license.
	 * </br>��Gamma�ֲ��������ȡһ����
	 * @param alpha Alpha parameter for Gamma Distribution.
	 * @param scale Scale parameter for Gamma Distribution.
	 * @throws IllegalArgumentException if arguments are out of range.
	 * @return The sample randomly drawn from the given distribution.
	 */
	public static double gammaRandom(double alpha, double scale) {
		Random r = new Random();
		
		double rate = 1 / scale;
		
		if (alpha <= 0.0 || rate <= 0.0) {
			throw new IllegalArgumentException();
		}
		
		double gds;
		double b = 0.0;
		
		// CASE A: Acceptance rejection algorithm gs
		if (alpha < 1.0) {
			b = 1.0 + 0.36788794412 * alpha; // Step 1
			while (true) {
				double p = b * r.nextDouble();
				// Step 2. Case gds <= 1
				if (p <= 1.0) {
					gds = Math.exp(Math.log(p) / alpha);
					if (Math.log(r.nextDouble()) <= -gds) {
						return gds / rate;
					}
				}
				// Step 3. Case gds > 1
				else {
					gds = -Math.log((b - p) / alpha);
					if (Math.log(r.nextDouble()) <= ((alpha - 1.0) * Math.log(gds))) {
						return gds / rate;
					}
				}
			}
		}
		// CASE B: Acceptance complement algorithm gd (gaussian distribution, box muller transformation)
		else {
			double ss = 0.0;
			double s = 0.0;
			double d = 0.0;

			// Step 1. Preparations
			if (alpha != -1.0) {
				ss = alpha - 0.5;
				s = Math.sqrt(ss);
				d = 5.656854249 - 12.0 * s;
			}
			
			// Step 2. Normal deviate
			double v12;
			double v1;
			
			do {
				v1 = 2.0 * r.nextDouble() - 1.0;
				double v2 = 2.0 * r.nextDouble() - 1.0;
				v12 = v1 * v1 + v2 * v2;
			} while (v12 > 1.0);
			
			double t = v1 * Math.sqrt(-2.0 * Math.log(v12) / v12);
			double x = s + 0.5 * t;
			gds = x * x;
			
			if (t >= 0.0) { // Immediate acceptance
				return gds / rate;
			}

			double u = r.nextDouble();
			if (d * u <= t * t * t) { // Squeeze acceptance
				return gds / rate;
			}

			double q0 = 0.0;
			double si = 0.0;
			double c = 0.0;
			
			// Step 4. Set-up for hat case
			if (alpha != -1.0) {
				double rr = 1.0 / alpha;
				double q9 = 0.0001710320;
				double q8 = -0.0004701849;
				double q7 = 0.0006053049;
				double q6 = 0.0003340332;
				double q5 = -0.0003349403;
				double q4 = 0.0015746717;
				double q3 = 0.0079849875;
				double q2 = 0.0208333723;
				double q1 = 0.0416666664;
				
				q0 = ((((((((q9 * rr + q8) * rr + q7) * rr + q6) * rr + q5) * rr + q4) *
						rr + q3) * rr + q2) * rr + q1) * rr;
				
				if (alpha > 3.686) {
					if (alpha > 13.022) {
						b = 1.77;
						si = 0.75;
						c = 0.1515 / s;
					}
					else {
						b = 1.654 + 0.0076 * ss;
						si = 1.68 / s + 0.275;
						c = 0.062 / s + 0.024;
					}
				}
				else {
					b = 0.463 + s - 0.178 * ss;
					si = 1.235;
					c = 0.195 / s - 0.079 + 0.016 * s;
				}
			}
			
			double v, q;
			double a9 = 0.104089866;
			double a8 = -0.112750886;
			double a7 = 0.110368310;
			double a6 = -0.124385581;
			double a5 = 0.142873973;
			double a4 = -0.166677482;
			double a3 = 0.199999867;
			double a2 = -0.249999949;
			double a1 = 0.333333333;
			
			// Step 5. Calculation of q
			if (x > 0.0) {
				// Step 6.
				v = t / (s + s);
				if (Math.abs(v) > 0.25) {
					q = q0 - s * t + 0.25 * t * t + (ss + ss) * Math.log(1.0 + v);
				}
				// Step 7. Quotient acceptance
				else {
					q = q0 + 0.5 * t * t * ((((((((a9 * v + a8) * v + a7) * v + a6) *
							v + a5) * v + a4) * v + a3) * v + a2) * v + a1) * v;
				}
				if (Math.log(1.0 - u) <= q) {
					return gds / rate;
				}
			}

			double e7 = 0.000247453;
			double e6 = 0.001353826;
			double e5 = 0.008345522;
			double e4 = 0.041664508;
			double e3 = 0.166666848;
			double e2 = 0.499999994;
			double e1 = 1.000000000;
			
			// Step 8. Double exponential deviate t
			while (true) {
				double sign_u;
				double e;
				do { // Step 9. Rejection of t
					e = -Math.log(r.nextDouble());
					u = r.nextDouble();
					u = u + u - 1.0;
					sign_u = (u > 0) ? 1.0 : -1.0;
					t = b + (e * si) * sign_u;
				} while (t <= -0.71874483771719);
				
				// Step 10. New q(t)
				v = t / (s + s);
				
				if (Math.abs(v) > 0.25) {
					q = q0 - s * t + 0.25 * t * t + (ss + ss) * Math.log(1.0 + v);
				}
				else {
					q = q0 + 0.5 * t * t * ((((((((a9 * v + a8) * v + a7) * v + a6) *
							v + a5) * v + a4) * v + a3) * v + a2) * v + a1) * v;
				}
				
				// Step 11.
				if (q <= 0.0) {
					continue;
				}
				
				// Step 12. Hat acceptance
				double w;
				if (q > 0.5) {
					w = Math.exp(q) - 1.0;
				}
				else {
					w = ((((((e7 * q + e6) * q + e5) * q + e4) * q + e3) * q + e2) *
							q + e1) * q;
				}
				
				if (c * u * sign_u <= w * Math.exp(e - 0.5 * t * t)) {
					x = s + 0.5 * t;
					return x * x / rate;
				}
			}
		}
	}
	
	/**
	 * Randomly sample several points from Gamma Distribution with the given parameters.
	 * 
	 * @param alpha Alpha parameter for Gamma Distribution.
	 * @param scale Scale parameter for Gamma Distribution.
	 * @param count The number of samples to draw.
	 * @return The sample randomly drawn from the given distribution.
	 */
	public static double[] gammaDistribution(double alpha, double scale, int count) {
		double[] result = new double[count];
		
		for (int i = 0; i < count; i++) {
			result[i] = gammaRandom(alpha, scale);
		}
		
		return result;
	}
	
}