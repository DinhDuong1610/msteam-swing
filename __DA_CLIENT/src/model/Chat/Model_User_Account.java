package model.Chat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.ImageIcon;

import org.json.JSONObject;

public class Model_User_Account {
	private int user_Id;
	private String userName;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private byte[] avatar;
	private boolean status;
	
	
	public Model_User_Account(int user_Id, String userName, String fullName , String email, String phone,
			String address, byte[] avatar, boolean status) {
		super();
		this.user_Id = user_Id;
		this.userName = userName;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.avatar = avatar;
		this.status = status;
	}
	
    public Model_User_Account(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            user_Id = obj.getInt("user_Id");
            userName = obj.getString("userName");
            fullName = obj.getString("fullName");
            email = obj.getString("email");
            phone = obj.getString("phone");
            address = obj.getString("address");
            avatar = convertHexStringToByteArray(obj.getString("avatar"));
            status = obj.getBoolean("status");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public JSONObject toJsonObject(String type) {
    	try {
			JSONObject json = new JSONObject();
			json.put("type", type);
			json.put("user_Id", user_Id);
			json.put("userName", userName);
			json.put("fullName", fullName);
			json.put("email", email);
			json.put("phone", phone);
			json.put("address", address);
			json.put("avatar", convertByteArrayToHexString(avatar));
			json.put("status", status);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    private String convertByteArrayToHexString(byte[] array) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : array) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private byte[] convertHexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                 + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
	
	public int getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Model_User_Account [user_Id=" + user_Id + ", userName=" + userName + ", fullName=" + fullName
				+ ", email=" + email + ", phone=" + phone + ", address=" + address + ", avatar="
				+ Arrays.toString(avatar) + ", status=" + status + "]";
	}
	
	

	
}
