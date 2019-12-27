import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChangeFile {
    public static void main(String[] args) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/home/cx/Downloads/vessel.bpmn20 (3).xml"))));//数据流读取文件

            StringBuffer strBuffer = new StringBuffer();
            for (String temp = null; (temp = bufReader.readLine()) != null; temp = null) {
                if(temp.indexOf("&lt;") != -1){ //判断当前行是否存在想要替换掉的字符 -1表示存在
                    temp = temp.replace("&lt;", "<");//替换为你想要的东东
                }
                if(temp.indexOf("&gt;") != -1){
                    temp = temp.replace("&gt;",">");
                }
                strBuffer.append(temp);
                strBuffer.append(System.getProperty("line.separator"));//行与行之间的分割
            }
            bufReader.close();
            PrintWriter printWriter = new PrintWriter("/home/cx/Desktop/vessel.bpmn20.xml");//替换后输出的文件位置
            printWriter.write(strBuffer.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}