package service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

import model.Model_Login;
import model.Model_Message;
import model.Model_Post;
import model.Model_Project;
import model.Model_Receive_Message;
import model.Model_Register;
import model.Model_Send_Message;
import model.Model_User_Account;

public class Service {
    private static Service instance;
    private ServerSocket serverSocket;
    private JTextArea textArea;
    private final int PORT_NUMBER = 1610;
	private ServiceUser serviceUser;
	private ServiceCommunity serviceCommunity;
	private ArrayList<ClientHandler> clients = new ArrayList<>();
    
    public static Service getInstance(JTextArea textArea) {
        if (instance == null) {
            instance = new Service(textArea);
        }
        return instance;
    }
    
    private Service(JTextArea textArea) {
        this.textArea = textArea;
        serviceUser = new ServiceUser();
    }

    public void startServer() {
        new Thread(() -> {
            try {
            	serverSocket = new ServerSocket(PORT_NUMBER);
                textArea.append("Server has started on port: " + PORT_NUMBER + "\n");
                
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    textArea.append("One client connected\n");
                    
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        ClientHandler clientHandler = new ClientHandler(System.currentTimeMillis()+"",this, in, out, clients);
                    }
                    catch (Exception e) {
                    	clientSocket.close();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void listen(ClientHandler client, String data) {
		serviceCommunity = new ServiceCommunity(client.getUserId()));
    	new Thread(()->{
    		try {
    			JSONObject jsonData = new JSONObject(data);
	            textArea.append("OO: " + jsonData);
    	    	if(jsonData.getString("type").equals("register")) {
    	            String userName = jsonData.getString("userName");
    	            String password = jsonData.getString("password");
    	            textArea.append("User has Register :" + userName + " Pass :" + password + "\n");
    	            Model_Register register = new Model_Register(userName, password);
    	            Model_Message message = serviceUser.register(register);
    	            broadcast(client.getUserId(), message.toJsonObject("register"));
    	            textArea.append("check Register :" + message.isAction() + "message:" + message.getMessage() );
    	    	}
    			else if(jsonData.getString("type").equals("login")) {
    	            String userName = jsonData.getString("userName");
    	            String password = jsonData.getString("password");
    	            textArea.append("User has Loginnn :" + userName + " Pass :" + password + "\n");
    	            Model_Login login = new Model_Login(userName, password);
    	            Model_Message message = serviceUser.login(login);
    	            broadcast(client.getUserId(), message.toJsonObject("login"));
    	            Model_User_Account account = serviceUser.loadLogin();
    	            broadcast(client.getUserId(), account.toJsonObject("loadLogin"));
    	            
    	            List<Model_User_Account> list = serviceUser.getUser();
    	            if(list.size() == 0) textArea.append("rong!!!!");
    	            for(Model_User_Account user : list) {    	    
    	            	broadcast(client.getUserId(), user.toJsonObject("list_user"));
//    	            	textArea.append("list user :" +  user.toJsonObject("list_user") + "\n");
    	            }
    	            client.setUserId(account.getUser_Id()+"");
    	            textArea.append("check Login :" + message.isAction() + "message:" + message.getMessage() );
    	            
    	    	}
    			else if(jsonData.getString("type").equals("sendMessage")) {
    			    int fromUserID = jsonData.getInt("fromUserID");
    			    int toUserID = jsonData.getInt("toUserID");
    			    String text = jsonData.getString("text");
    	            textArea.append("Send message form :" + fromUserID + " -to- " + toUserID + " : " + text + "\n");
    	            Model_Send_Message sendMessage = new Model_Send_Message(fromUserID, toUserID, text);
    	            Model_Receive_Message receiveMessage = new Model_Receive_Message(sendMessage.getFromUserID(), sendMessage.getText());
    	            broadcast(toUserID+"", receiveMessage.toJsonObject("receiveMessage"));
    	            textArea.append("Receive message form :" + fromUserID + " -to- " + toUserID + " : " + text + "\n");
    	    	}
    			else if(jsonData.getString("type").equals("addProject")) {
    				String projectName = jsonData.getString("projectName");
    				Model_Project project = serviceCommunity.addProject(projectName);
    	            broadcast(client.getUserId(), project.toJsonObject("addProject"));
    	            textArea.append("Add project :" + project.toJsonObject("addProject") + "\n");
    	           
    			}
    			else if(jsonData.getString("type").equals("listProject")) {
	            	textArea.append("list project :" +  jsonData + "\n");
    	            List<Model_Project> list = serviceCommunity.getProject(Integer.parseInt(client.getUserId()));
    	            if(list.size() == 0) textArea.append("rong!!!!");
    	            for(Model_Project pro : list) {    	    
    	            	broadcast(client.getUserId(), pro.toJsonObject("listProject"));
    	            	textArea.append("list project :" +  pro.toJsonObject("listProject") + "\n");
    	            }
    	            textArea.append("list project DONE \n");
    			}
    			else if(jsonData.getString("type").equals("postNews")) {
    				Model_Post post = new Model_Post(jsonData);
    				serviceCommunity = new ServiceCommunity(Integer.parseInt(client.getUserId()));
    				Model_Post post1 = serviceCommunity.postNews(post);
    				broadcastCommunity(post.getProjectId(), post1.toJsonObject("postNews"));
    	            textArea.append("Post new :" + post1.toJsonObject("postNews") + "\n");
    			}
    			else if(jsonData.getString("type").equals("listPost")) {
	            	textArea.append("list post :" +  jsonData + "\n");
	            	textArea.append("project id :" +  jsonData.getInt("projectId") + "\n");
    	            List<Model_Post> list = serviceCommunity.getPost(jsonData.getInt("projectId"));
    	            if(list.size() == 0) textArea.append("rong!!!!\n");
    	            for(Model_Post post : list) {    	    
    	            	broadcast(client.getUserId(), post.toJsonObject("listPost"));
    	            	textArea.append("list post :" +  post.toJsonObject("listPost") + "\n");
    	            }
    	            textArea.append("list project DONE \n");
    			}
    			else if(jsonData.getString("type").equals("listMember")) {
	            	textArea.append("list member :" +  jsonData + "\n");
    	            List<Model_User_Account> list = serviceCommunity.getMember(jsonData.getInt("projectId"));
    	            if(list.size() == 0) textArea.append("rong!!!!");
    	            for(Model_User_Account user : list) {    	    
    	            	broadcastCommunity(jsonData.getInt("projectId"), user.toJsonObject("listMember"));
    	            	textArea.append("list member :" +  user.toJsonObject("listMember") + "\n");
    	            }
    			}
    			else if(jsonData.getString("type").equals("addMember")) {
    				String userName = jsonData.getString("userName");
    				int projectId = jsonData.getInt("projectId");
    				Model_User_Account user = serviceCommunity.addMember(userName, projectId);
    				broadcastCommunity(jsonData.getInt("projectId"), user.toJsonObject("addMember"));
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	}).start();
    }
    
    public synchronized void broadcast(String userId, JSONObject jsonData) {
//    	new Thread(()-> {
            for (ClientHandler client : clients) {
                if(client.getUserId().equals(userId)) {
                	client.sendMessage(jsonData);
                }
            }
//    	}).start();
    }
    
    public synchronized void broadcastMessage(String userId, JSONObject jsonData) {
//    	new Thread(()-> {
            for (ClientHandler client : clients) {
                if(client.getUserId().equals(userId)) {
                	client.sendMessage(jsonData);
                }
            }
//    	}).start();
    }
	
    public synchronized void broadcastCommunity(int projectId, JSONObject jsonData) {
    	List<Model_User_Account> list = new ServiceCommunity(1).getMember(projectId);
//    	new Thread(()-> {
            for (ClientHandler client : clients) {
                for(Model_User_Account account : list) {
                	if(client.getUserId().equals(account.getUser_Id()+"")) {
                		client.sendMessage(jsonData);
                		break;
                	}
                }
            }
//    	}).start();
    }
	
}
