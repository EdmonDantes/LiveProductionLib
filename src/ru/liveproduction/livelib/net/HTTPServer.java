/*
Copyright © 2019 Ilya Loginov. All rights reserved.
Please email dantes2104@gmail.com if you would like permission to do something with the contents of this repository
*/

package ru.liveproduction.livelib.net;

import ru.liveproduction.livelib.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Pattern;

public class HTTPServer {

    private ServerSocket serverSocket = null;
    private Thread thread = null;
    private boolean start = false;

    private void init(int port, int maxConnection, InetAddress address) throws IOException {
        this.serverSocket = new ServerSocket(port, maxConnection, address);
    }

    public static class Request {
        enum Methods {
            OPTIONS,
            GET,
            HEAD,
            POST,
            PUT,
            PATCH,
            DELETE,
            TRACE,
            CONNECT
        }

        Methods method = null;
        String address = null;
        Map<String, String> headers = new TreeMap<>();
        Map<String, String> query = new TreeMap<>();
        String body = null;
        boolean https = false;

        public Request(Socket socket) throws IOException {
            BufferedReader readingStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            LinkedList<String> headParts = new LinkedList<>();
            while(true) {
                String tmp = readingStream.readLine();
                if(tmp == null || tmp.trim().length() == 0) {
                    break;
                } else {
                    headParts.addLast(tmp);
                }
            }

            List<String> mainInformation = StringUtils.split(headParts.pollFirst(), new String[]{" "});
            if (mainInformation.size() > 1) {
                this.method = Methods.valueOf(mainInformation.get(0));
                this.address = mainInformation.get(1).split(Pattern.quote("?"))[0];
                String[] query0 = mainInformation.get(1).split(Pattern.quote("?"));
                if (query0.length > 1) {
                    String[] query = query0[1].replace(" ", "").split(Pattern.quote("&"));
                    for (var obj : query) {
                        String[] value = obj.split(Pattern.quote("="));
                        if (value.length > 1) {
                            this.query.put(value[0], value[1]);
                        }
                    }
                }
                https = mainInformation.get(2).split(Pattern.quote("/"))[0].equals("HTTPS");
            }
            while (headParts.size() > 0) {
                String tmp = headParts.pollFirst();
                if (tmp == null || tmp.length() < 1) break;
                int index = tmp.indexOf(':');
                headers.put(tmp.substring(0, index), tmp.substring(index + 1).trim());
            }
            StringBuilder bodyBuilder = new StringBuilder();
            while (readingStream.ready()) {
                bodyBuilder.append((char) readingStream.read());
            }
            body = bodyBuilder.toString();
        }

        public String toString(){
            StringBuilder result = new StringBuilder();
            if (method == null) {
                return "NULL REQUEST";
            }

            result.append(method.toString()).append(" ").append(address).append(" ").append("HTTP/1.1").append("\r\n");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                result.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
            }
            result.append("\r\n");
            result.append(body);
            return result.toString();
        }

        public Methods getMethod() {
            return method;
        }

        public String getAddress() {
            return address;
        }

        public Map<String, String> getQuery() {
            return query;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getHeader(String header) {
            return headers.get(header);
        }

        public String getBody() {
            return body;
        }
    }

    public static class Resolve {
        int code;
        Map<String, String> headers;
        String body;

        public Resolve(){
            this.code = 200;
            this.headers = new TreeMap<>();
            this.body = "";
        }

        public Resolve(int code) {
            this.code = code;
            this.headers = new TreeMap<>();
            this.body = "";
        }

        public Resolve(int code, String body){
            this.code = code;
            this.body = body;
            this.headers = new TreeMap<>();
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void addHeader(String header, String value) {
            headers.put(header, value);
        }

        public int getCode() {
            return code;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getBody() {
            return body;
        }

        public String getHeader(String header) {
            return headers.get(header);
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("HTTP/1.1 ").append(code).append(" ");
            switch (code) {
                case 100: result.append("Continue"); break;
                case 101: result.append("Switching Protocols"); break;
                case 102: result.append("Processing"); break;
                case 200: result.append("OK"); break;
                case 201: result.append("Created"); break;
                case 202: result.append("Accepted"); break;
                case 203: result.append("Non-Authoritative Information"); break;
                case 204: result.append("No Content"); break;
                case 205: result.append("Reset Content"); break;
                case 206: result.append("Partial Content"); break;
                case 207: result.append("Multi-Status"); break;
                case 208: result.append("Already Reported"); break;
                case 226: result.append("IM Used"); break;
                case 300: result.append("Multiple Choices"); break;
                case 301: result.append("Moved Permanently"); break;
                case 302: result.append("Moved Temporarily"); break;
                case 303: result.append("See Other"); break;
                case 304: result.append("Not Modified"); break;
                case 305: result.append("Use Proxy"); break;
                case 306: result.append("NULL"); break;
                case 307: result.append("Temporary Redirect"); break;
                case 308: result.append("Permanent Redirect"); break;
                case 400: result.append("Bad Request"); break;
                case 401: result.append("Unauthorized"); break;
                case 402: result.append("Payment Required"); break;
                case 403: result.append("Forbidden"); break;
                case 404: result.append("Not Found"); break;
                case 405: result.append("Method Not Allowed"); break;
                case 406: result.append("Not Acceptable"); break;
                case 407: result.append("Proxy Authentication Required"); break;
                case 408: result.append("Request Timeout"); break;
                case 409: result.append("Conflict"); break;
                case 410: result.append("Gone"); break;
                case 411: result.append("Length Required"); break;
                case 412: result.append("Precondition Failed"); break;
                case 413: result.append("Payload Too Large"); break;
                case 414: result.append("URI Too Long"); break;
                case 415: result.append("Unsupported Media Type"); break;
                case 416: result.append("Range Not Satisfiable"); break;
                case 417: result.append("Expectation Failed"); break;
                case 418: result.append("I’m a teapot"); break;
                case 419: result.append("Authentication Timeout (not in RFC 2616)"); break;
                case 421: result.append("Misdirected Request"); break;
                case 422: result.append("Unprocessable Entity"); break;
                case 423: result.append("Locked"); break;
                case 424: result.append("Failed Dependency"); break;
                case 426: result.append("Upgrade Required"); break;
                case 428: result.append("Precondition Required"); break;
                case 429: result.append("Too Many Requests"); break;
                case 431: result.append("Request Header Fields Too Large"); break;
                case 449: result.append("Retry With"); break;
                case 451: result.append("Unavailable For Legal Reasons"); break;
                case 499: result.append("Client Closed Request"); break;
                case 500: result.append("Internal Server Error"); break;
                case 501: result.append("Not Implemented"); break;
                case 502: result.append("Bad Gateway"); break;
                case 503: result.append("Service Unavailable"); break;
                case 504: result.append("Gateway Timeout"); break;
                case 505: result.append("HTTP Version Not Supported"); break;
                case 506: result.append("Variant Also Negotiates"); break;
                case 507: result.append("Insufficient Storage"); break;
                case 508: result.append("Loop Detected"); break;
                case 509: result.append("Bandwidth Limit Exceeded"); break;
                case 510: result.append("Not Extended"); break;
                case 511: result.append("Network Authentication Required"); break;
                case 520: result.append("Unknown Error"); break;
                case 521: result.append("Web Server Is Down"); break;
                case 522: result.append("Connection Timed Out"); break;
                case 523: result.append("Origin Is Unreachable"); break;
                case 524: result.append("A Timeout Occurred"); break;
                case 525: result.append("SSL Handshake Failed"); break;
                case 526: result.append("Invalid SSL Certificate"); break;
            }
            result.append("\r\n");
            for (var obj : headers.entrySet()) {
                result.append(obj.getKey()).append(": ").append(obj.getValue()).append("\r\n");
            }
            result.append("\r\n");
            result.append(body);
            return result.toString();
        }

        public void send(Socket socket) throws IOException {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(this.toString());
            printWriter.flush();
            printWriter.close();
        }
    }

    //TODO: create handler for client
    public interface ClientHandler {

    }

    public HTTPServer(){
        try {
            init(0, -1, null);
            System.out.println("Server created on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            this.thread = null;
            System.err.println("Server can`t created");
        }
    }

    public HTTPServer(int port) {
        try {
            init(port, -1, null);
            System.out.println("Server created on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            this.thread = null;
            System.err.println("Server can`t created");
        }
    }

    public HTTPServer(int port, int maxConnections) {
        try {
            init(port, maxConnections, null);
            System.out.println("Server created on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            this.thread = null;
            System.err.println("Server can`t created");
        }
    }

    public HTTPServer(int port, int maxConnections, InetAddress address){
        try {
            init(port, maxConnections, address);
            System.out.println("Server created on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            this.thread = null;
            System.err.println("Server can`t created");
        }
    }

    public boolean start() {
        if (this.serverSocket != null) {
            this.thread = new Thread(() -> {
                while (start) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println(new Request(socket).toString());
                        Resolve resolve = new Resolve(200);
                        resolve.setCode(200);
                        resolve.addHeader("Server", "LIVeProduction.LiveLib.Net.HTTPServer/0.1v");
                        resolve.addHeader("Date", new Date().toString());
                        resolve.send(socket);
                        socket.close();
                    } catch (IOException e) {
                        System.err.println("Can`t read request from port: " + serverSocket.getLocalPort());
                        e.printStackTrace();
                    }
                }
            });
            this.start = true;
            this.thread.start();
            System.out.println("Server started");
        }else {
            System.err.println("Server can`t started");
        }
        return start;
    }

}

