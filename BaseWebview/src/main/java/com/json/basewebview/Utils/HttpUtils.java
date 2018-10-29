package com.json.basewebview.Utils;

/**
 * Created by user on 2018/6/28.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//Http请求的工具类
public class HttpUtils {

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public interface CallBack {
        void onRequestComplete(String result);

        void onError();
    }


    /**
     * 异步的Get请求
     *
     * @param urlStr
     * @param callBack
     */
    public static void doGetAsyn(final String urlStr, final CallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = doGet(urlStr, callBack);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    callBack.onError();
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 异步的Post请求
     *
     * @param urlStr
     * @param params
     * @param callBack
     * @throws Exception
     */
    public static void doPostAsyn(final String urlStr, final String params,
                                  final CallBack callBack){
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = doPost(urlStr, params,callBack);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    callBack.onError();
                    e.printStackTrace();
                }

            }
        }.start();

    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @param callBack
     * @return
     * @throws Exception
     */
    private static String doGet(String urlStr, CallBack callBack) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            callBack.onError();
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
            conn.disconnect();
        }

        return null;

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param callBack
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    private static String doPost(String url, String param, CallBack callBack) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            if (param != null && !param.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            callBack.onError();
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void get(String httpurl, CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream is = null;
                BufferedReader br = null;
                String result = null;// 返回结果字符串
                try {
                    // 创建远程url连接对象
                    URL url = new URL(httpurl);
                    // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置连接方式：get
                    connection.setRequestMethod("GET");
                    // 设置连接主机服务器的超时时间：15000毫秒
                    connection.setConnectTimeout(15000);
                    // 设置读取远程返回的数据时间：60000毫秒
                    connection.setReadTimeout(60000);
                    // 发送请求
                    connection.connect();
                    // 通过connection连接，获取输入流
                    if (connection.getResponseCode() == 200) {
                        is = connection.getInputStream();
                        // 封装输入流is，并指定字符集
                        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        // 存放数据
                        StringBuffer sbf = new StringBuffer();
                        String temp = null;
                        while ((temp = br.readLine()) != null) {
                            sbf.append(temp);
                            sbf.append("\r\n");
                        }
                        result = sbf.toString();
                    }
                } catch (Exception e) {
                    callBack.onError();
                    e.printStackTrace();
                } finally {
                    // 关闭资源
                    if (null != br) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (null != is) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    connection.disconnect();// 关闭远程连接
                }
                callBack.onRequestComplete(result);
            }
        }).start();
    }

    public static void post(String httpUrl, String param, CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream is = null;
                OutputStream os = null;
                BufferedReader br = null;
                String result = null;
                try {
                    URL url = new URL(httpUrl);
                    // 通过远程url连接对象打开连接
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置连接请求方式
                    connection.setRequestMethod("POST");
                    // 设置连接主机服务器超时时间：15000毫秒
                    connection.setConnectTimeout(15000);
                    // 设置读取主机服务器返回数据超时时间：60000毫秒
                    connection.setReadTimeout(60000);

                    // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
                    connection.setDoOutput(true);
                    // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
                    connection.setDoInput(true);
                    // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
                    connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
                    // 通过连接对象获取一个输出流
                    os = connection.getOutputStream();
                    // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
                    os.write(param.getBytes());
                    // 通过连接对象获取一个输入流，向远程读取
                    if (connection.getResponseCode() == 200) {

                        is = connection.getInputStream();
                        // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                        StringBuffer sbf = new StringBuffer();
                        String temp = null;
                        // 循环遍历一行一行读取数据
                        while ((temp = br.readLine()) != null) {
                            sbf.append(temp);
                            sbf.append("\r\n");
                        }
                        result = sbf.toString();
                    }
                } catch (Exception e) {
                    callBack.onError();
                    e.printStackTrace();
                } finally {
                    // 关闭资源
                    if (null != br) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != os) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != is) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // 断开与远程地址url的连接
                    connection.disconnect();
                }
                callBack.onRequestComplete(result);
            }
        }).start();
    }
}
