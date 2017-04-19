package com.swust.kelab.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;
import org.apache.commons.codec.binary.Base64;

/**
 * 字符串的Base64压缩与解压缩工具类
 * @author yangzq
 *
 */
public class CompressUtil {

	//压缩字符串  
    public static String compressData(String data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);  
            zos.write(data.getBytes());  
            zos.close();  
            return new String(getenBASE64inCodec(bos.toByteArray()));  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return "ZIP_ERR";  
        }
    }

    //使用apche codec对数组进行encode  
    public static String getenBASE64inCodec(byte [] b) {
        if (b == null)  
            return null;  
        return new String((new Base64()).encode(b));  
    }

    //base64转码为string  
    public static byte[] getdeBASE64inCodec(String s) {
        if (s == null)  
            return null;  
        return new Base64().decode(s.getBytes());  
    }

    //解码字符串  
    public static String decompressData(String encdata) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InflaterOutputStream zos = new InflaterOutputStream(bos);
            zos.write(getdeBASE64inCodec(encdata));
            zos.close();
            return new String(bos.toByteArray());  
        } catch (Exception ex) { 
            ex.printStackTrace();
            return "UNZIP_ERR";  
        }
    }
    
    public static void main(String[] args) {
    	String de = "eJzVV01v4zYQ%2FStFDntS15Zkx3KAxSJ20qJA0uxu0vZQBAEt0RYTSlRJyokQ5L93hpQoy1biJNsC7cmaecPhmw8O6Uf%2F6M9Hf3x0wFmuUnZ48OQ9%2BqNGHB88XXsBWhweHQxiUUpFB2tG7%2Bfm84wp%2FTER3hklMmf56iZOhVDUgh9TnXGPN9CaJVQYHfoM0WcEPhcL9UUoPYglJZrOrGRITDZQRTX%2BXlUF9ZwSedQLkEgLJJRTTfGz1%2FiEasK4gb7RglcGM1%2FOjdU7TkZsfZEk%2BZ0ppn8tM2%2FABUkGibjP8aMWy8IKRGsSpxnNG04Y%2BghCD1zCQ4w1gITb5F5RyGiTpDFa%2Bn2JV7PqN0WlN1hJktA5J0oZ9GcnzqoTWhAJ4SjYOdbz1oWNkq4JLyG8c6pTkRjgmPPTNbEKZWowfWFzQzxoDWy2jHbYRDc0YohGKSk0lc0%2BcyvWDfQ5IZp8ioi65XfL2yDyOVnHUKFmESS8XjCrjpOM5dh0m%2BiFPBeNyR9Mp9%2BoAlIx7dhBCXa8YJoPMc3Q36pSGclv5q4QN%2BDZdvFAS8Jy9HD6wHJv15ImrKkbBDxxzr6WgDKRqz5fDvRYVgipIQbCvZ2VXd%2FTJre%2B79mPoHGpUoG5vYyFpBvbQN8ZlVkeOWpfSEHlj%2FBVEm6ZdQBSapERzeJt1gZW9d4%2BJnACCQyh5oBiy5l%2BxzqfOFF5%2FcOj7dJnDjVyDv23ulbgwXZ5DcH8sAgAl5roUiHvCHnDSfxhYIbTJZVraBOIi0ptBa8LlQU0Ku2F6jz3QfYAYmGMbEKCAz%2BQdZPW88cKSPsnIZv%2B%2FFzAWb4XMvl0e3dLMmDmB1OVQ8ndamisZvHm6XB4Jl7GgfkubkiG%2FweSwX%2Be5LU3xUabtKPSNov3iqaeQ7%2BthKzc3ARZ7R%2Fe%2B%2B4hrzu0O0eouR5rFQTNllW7wJ1dzP7oXafe1A2vlq0wIP%2BnHQ2kz98Jts8q2LHCUb1tZe7aMHod5b9KKivL%2BWI5T2l814Uucmq01qTNT61TZtyG7bPJVbKbcqfu5tmpm3cD5PrNY%2FA4T64oSWMqf8mX0KhAyl7AeH3DLY7PKbBBJ5uTEtZAFPJKWC%2FYwP4QOniE82DjweHmmhE7bxE7Q3f1NppdvR2s5tuEGuwJdQNT8MCM01l1Xmp2RWXWAaFX2nUdxDJ5zqm9j08fYso7evrg9CYt%2BHgeYY0JnnZMK2a6dt6Iqs58WcvnJCerF0%2B%2FycHk33E7fqfb3pP8mhH2zJVvyESdh%2BJotKfs7%2BEt6Qp2BcC0mz0IThdjx1Fltc%2FMKigz%2FgMaTfGhoKqCzEWupeC8fc1egtbbAevXAigxuDH2NBcrOz3OxMoM6VXdUiBb0QaFsFmEZ35B8rzda2Ykr9GC%2FZYGet5qjAMc0Fu8wOCM5TDOevn2QjWrPqgrmj1DN%2FTwrQVJZPGg4EQvhcxMPvHf3%2Fjw%2Bx6M2xtoO7UMgXF3UP3D%2F4zMpf8dzDvb2vKDkGv3SB%2FvOwdvTI3SpDar%2F4zv5XD99OEDmcbRdBIulouJH1KfJNPFOEom0TAakuVyNPkb1FsFgQ%3D%3D";
		String pe = "eJzVV01v4zYQ%2FStFDntS15Zkx3KAxSJ20qJA0uxu0vZQBAEt0RYTSlRJyokQ5L93hpQoy1biJNsC7cmaecPhmw8O6Uf%2F6M9Hf3x0wFmuUnZ48OQ9%2BqNGHB88XXsBWhweHQxiUUpFB2tG7%2Bfm84wp%2FTER3hklMmf56iZOhVDUgh9TnXGPN9CaJVQYHfoM0WcEPhcL9UUoPYglJZrOrGRITDZQRTX%2BXlUF9ZwSedQLkEgLJJRTTfGz1%2FiEasK4gb7RglcGM1%2FOjdU7TkZsfZEk%2BZ0ppn8tM2%2FABUkGibjP8aMWy8IKRGsSpxnNG04Y%2BghCD1zCQ4w1gITb5F5RyGiTpDFa%2Bn2JV7PqN0WlN1hJktA5J0oZ9GcnzqoTWhAJ4SjYOdbz1oWNkq4JLyG8c6pTkRjgmPPTNbEKZWowfWFzQzxoDWy2jHbYRDc0YohGKSk0lc0%2BcyvWDfQ5IZp8ioi65XfL2yDyOVnHUKFmESS8XjCrjpOM5dh0m%2BiFPBeNyR9Mp9%2BoAlIx7dhBCXa8YJoPMc3Q36pSGclv5q4QN%2BDZdvFAS8Jy9HD6wHJv15ImrKkbBDxxzr6WgDKRqz5fDvRYVgipIQbCvZ2VXd%2FTJre%2B79mPoHGpUoG5vYyFpBvbQN8ZlVkeOWpfSEHlj%2FBVEm6ZdQBSapERzeJt1gZW9d4%2BJnACCQyh5oBiy5l%2BxzqfOFF5%2FcOj7dJnDjVyDv23ulbgwXZ5DcH8sAgAl5roUiHvCHnDSfxhYIbTJZVraBOIi0ptBa8LlQU0Ku2F6jz3QfYAYmGMbEKCAz%2BQdZPW88cKSPsnIZv%2B%2FFzAWb4XMvl0e3dLMmDmB1OVQ8ndamisZvHm6XB4Jl7GgfkubkiG%2FweSwX%2Be5LU3xUabtKPSNov3iqaeQ7%2BthKzc3ARZ7R%2Fe%2B%2B4hrzu0O0eouR5rFQTNllW7wJ1dzP7oXafe1A2vlq0wIP%2BnHQ2kz98Jts8q2LHCUb1tZe7aMHod5b9KKivL%2BWI5T2l814Uucmq01qTNT61TZtyG7bPJVbKbcqfu5tmpm3cD5PrNY%2FA4T64oSWMqf8mX0KhAyl7AeH3DLY7PKbBBJ5uTEtZAFPJKWC%2FYwP4QOniE82DjweHmmhE7bxE7Q3f1NppdvR2s5tuEGuwJdQNT8MCM01l1Xmp2RWXWAaFX2nUdxDJ5zqm9j08fYso7evrg9CYt%2BHgeYY0JnnZMK2a6dt6Iqs58WcvnJCerF0%2B%2FycHk33E7fqfb3pP8mhH2zJVvyESdh%2BJotKfs7%2BEt6Qp2BcC0mz0IThdjx1Fltc%2FMKigz%2FgMaTfGhoKqCzEWupeC8fc1egtbbAevXAigxuDH2NBcrOz3OxMoM6VXdUiBb0QaFsFmEZ35B8rzda2Ykr9GC%2FZYGet5qjAMc0Fu8wOCM5TDOevn2QjWrPqgrmj1DN%2FTwrQVJZPGg4EQvhcxMPvHf3%2Fjw%2Bx6M2xtoO7UMgXF3UP3D%2F4zMpf8dzDvb2vKDkGv3SB%2FvOwdvTI3SpDar%2F4zv5XD99OEDmcbRdBIulouJH1KfJNPFOEom0TAakuVyNPkb1FsFgQ%3D%3D";
    	System.out.println();
		System.out.println(decompressData(pe));
	}
}
