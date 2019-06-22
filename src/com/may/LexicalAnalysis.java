package com.may;

import java.util.*;
import java.io.*;
import java.lang.String;

public class LexicalAnalysis {

    private static String[] keyword = new String[]{"if", "else", "int",
            "return", "void", "while"};

    private static char[] delimiter = new char[]{'(', ')', '*', '/',
            '+', '-', '<', '=',
            '>', '!', ';', ',',
            '[', ']', '{', '}'};

    private static File f;
    private static BufferedWriter bw;//写缓冲
    private static BufferedReader br;//读缓冲
    private static StringBuilder sb=new StringBuilder();

    //判断是否为关键字
    public static boolean isKeyword(String str) {

        for (int i = 0; i < keyword.length; i++) {
            if (keyword[i].equals(str)) {
                return true;
            }
        }

        return false;
    }

    //判断是否为数字
    public static boolean isNum(char ch) {

        if (ch >= '0' && ch <= '9') {
            return true;
        }

        return false;
    }

    //判断是否为字母
    public static boolean isLetter(char ch) {

        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            return true;
        }

        return false;
    }

    //判断是否为专用字符
    public static boolean isDelimiter(char ch) {


        for (int i = 0; i < delimiter.length; i++) {
            if (ch == delimiter[i]) {
                return true;
            }
        }
        return false;
    }

    public static StringBuilder readFile(StringBuilder sb, String fileSrc) {//读文件

        try {
            FileReader fileReader = new FileReader(fileSrc);
            BufferedReader br = new BufferedReader(fileReader);
            String temp = null;

            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static void lexicalAnalysis(String filePath) {//进行词法分析
        int i=0;

        try {
            f = new File("result.txt");
            bw = new BufferedWriter(new FileWriter(f));
            br = new BufferedReader(new FileReader(filePath));

            bw.write("*****************************************" + "\r\n" +
                    "C-Minus Lexical Analazer Result:" + "\r\n" +
                    "*****************************************" + "\r\n");

            StringBuilder line=readFile(sb,filePath);//文件内容存放于容器中
            System.out.println(line.toString());

            while (i< line.length()) {  //对容器进行读取

                char ch = line.charAt(i);

                if (isLetter(ch)) {//判断第一个是否是字母

                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(ch);

                    ch = line.charAt(++i);//读取下一个字符

                    while (isLetter(ch) || isNum(ch)) {

                        sb1.append(ch);
                        if (i == line.length() - 1) {//读到行末尾
                            ++i;
                            break;
                        }
                        else
                            ch = line.charAt(++i);
                    }

                    //判断当前字符串是否是关键字
                    if (isKeyword(sb1.toString())) {

                        bw.write("          reserved word:" + sb1.toString());
                        bw.newLine();//换行
                    }
                    //标识符，即为ID
                    else {
                        bw.write("          ID,name=" + sb1.toString());
                        bw.newLine();
                    }
                }

                //如果第一个是分界符
                else if (isDelimiter(ch)) {

                    StringBuilder sb1 = new StringBuilder();

                    //如果是逗号，分号，直接写入
                    if (ch == ';' || ch == ',' || ch == '=') {
                        bw.write("          "+ch);
                        bw.newLine();
                        i++;
                    }
                    //直接写入
                    else if (ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}' ) {
                        bw.write("          "+ch);
                        bw.newLine();
                        i++;
                    }
                    else if (ch == '+' || ch == '-' || ch == '*') {
                        bw.write("          "+ch);
                        bw.newLine();
                        i++;
                    }
                    //判断是否为双分界符
                    else if (ch == '>' || ch == '<' || ch == '=') {

                        sb1.append(ch);
                        char nextCh = line.charAt(++i);

                        if (nextCh == '=') {
                            sb1.append(nextCh);
                            bw.write("          "+sb1.toString());
                            bw.newLine();
                            i++;
                        }
                        else {
                            bw.write("          "+ch);
                            bw.newLine();
                        }
                    }

                    //判断注释
                    else if (ch == '/') {
                        sb1.append(ch);

                        if (i == line.length() - 1) {
                            break;
                        }
                        //继续读取下一个字符
                        ch = line.charAt(++i);

                        if(ch == '*') {//为/*注释
                            while(true){
                                ch = line.charAt(++i);
                                if(ch == '*'){// 为多行注释结束
                                    ch = line.charAt(++i);
                                    if(ch == '/') {
                                        ch = line.charAt(++i);
                                        break;
                                    }
                                }
                            }
                        }
                        else{//否则为 /
                            bw.write("          "+sb1.toString());
                            bw.newLine();
                        }
                    }
                }
                //数字
                else if (isNum(ch)) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(ch);
                    ch = line.charAt(++i);

                    if (isNum(ch)) {
                        while (isNum(ch)) {
                            sb1.append(ch);
                            ch = line.charAt(++i);
                        }
                        bw.write("          NUM:" + sb1.toString());
                        bw.newLine();
                    }
                    else {
                        bw.write("          NUM:" + sb1.toString());
                        bw.newLine();
                    }

                    if (isLetter(ch)) {
                        System.out.println("error:非法字符");
                    }
                }
                else
                    i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bw.close();
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}

