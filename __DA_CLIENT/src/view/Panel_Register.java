package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;

import model.ImageDecoder;
import model.ImageEncoder;
import model.Chat.Model_User_Account;
import service.Service;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.event.ActionEvent;

public class Panel_Register extends JPanel{
	private JLabel label_user_avatar;
	private JLabel label_user_TenTaiKhoan;
	private JLabel label_user_HoVaTen;
	private JLabel label_user_email;
	private JLabel label_user_sdt;
	private JLabel label_user_DiaChi;
	private JTextField tf_user_TenTaiKhoan;
	private JTextField tf_user_HoVaTen;
	private JTextField tf_user_email;
	private JTextField tf_user_sdt;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;
	private JButton bt_chonAnh;
	private JButton bt_save;
	
	private byte[] image;
	private JDialog dialog;

	public Panel_Register(JDialog dialog, String userName) {
		this.dialog = dialog;
		setSize(1390, 585);
		setLayout(null);
		
		label_user_avatar = new JLabel("");
		label_user_avatar.setIcon(new ImageIcon((new ImageIcon((MainUI.class.getResource("/images/background/avatar.jpg"))).getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH))));
		label_user_avatar.setBounds(83, 87, 350, 350);
		add(label_user_avatar);
		
		label_user_TenTaiKhoan = new JLabel("TÊN TÀI KHOẢN");
		label_user_TenTaiKhoan.setFont(new Font("Segoe UI", Font.BOLD, 26));
		label_user_TenTaiKhoan.setBounds(503, 87, 217, 48);
		add(label_user_TenTaiKhoan);
		
		label_user_HoVaTen = new JLabel("HỌ VÀ TÊN");
		label_user_HoVaTen.setFont(new Font("Segoe UI", Font.BOLD, 26));
		label_user_HoVaTen.setBounds(503, 145, 217, 48);
		add(label_user_HoVaTen);
		
		label_user_email = new JLabel("EMAIL");
		label_user_email.setFont(new Font("Segoe UI", Font.BOLD, 26));
		label_user_email.setBounds(503, 203, 217, 48);
		add(label_user_email);
		
		label_user_sdt = new JLabel("SỐ ĐIỆN THOẠI");
		label_user_sdt.setFont(new Font("Segoe UI", Font.BOLD, 26));
		label_user_sdt.setBounds(503, 261, 217, 48);
		add(label_user_sdt);
		
		label_user_DiaChi = new JLabel("ĐỊA CHỈ");
		label_user_DiaChi.setFont(new Font("Segoe UI", Font.BOLD, 26));
		label_user_DiaChi.setBounds(503, 319, 217, 48);
		add(label_user_DiaChi);
		
		tf_user_TenTaiKhoan = new JTextField(userName);
		tf_user_TenTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		tf_user_TenTaiKhoan.setColumns(10);
		tf_user_TenTaiKhoan.setBounds(745, 87, 575, 48);
		add(tf_user_TenTaiKhoan);
		
		tf_user_HoVaTen = new JTextField();
		tf_user_HoVaTen.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		tf_user_HoVaTen.setColumns(10);
		tf_user_HoVaTen.setBounds(745, 145, 575, 48);
		add(tf_user_HoVaTen);
		
		tf_user_email = new JTextField();
		tf_user_email.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		tf_user_email.setColumns(10);
		tf_user_email.setBounds(745, 203, 575, 48);
		add(tf_user_email);
		
		tf_user_sdt = new JTextField();
		tf_user_sdt.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		tf_user_sdt.setColumns(10);
		tf_user_sdt.setBounds(745, 261, 575, 48);
		add(tf_user_sdt);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setBounds(745, 319, 575, 121);
		add(scrollPane);
		
		JTextArea ta_DiaChi = new JTextArea();
		ta_DiaChi.setWrapStyleWord(true);
		ta_DiaChi.setLineWrap(true);
		ta_DiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		scrollPane.setViewportView(ta_DiaChi);
		
		lblNewLabel = new JLabel("REGISTER");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(518, 10, 437, 48);
		add(lblNewLabel);
		
		bt_chonAnh = new JButton("");
		bt_chonAnh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image = ImageEncoder.encodeImageToBytes(label_user_avatar);
				if (image != null) {
		            System.out.println("Ảnh đã được chọn thành công.");
		            System.out.println(image.toString());
		        } else {
		            System.out.println("Không thể chọn ảnh hoặc xảy ra lỗi.");
		        }
			}
		});
		bt_chonAnh.setIcon(new ImageIcon(Panel_Register.class.getResource("/images/icon/icon_camera.png")));
		bt_chonAnh.setBounds(83, 436, 350, 36);
		add(bt_chonAnh);
		
		bt_save = new JButton("SAVE");
		bt_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(image == null) {
					System.out.println("Ảnh chưa được chọn!");
				}
				Model_User_Account user = new Model_User_Account(999, tf_user_TenTaiKhoan.getText(), tf_user_HoVaTen.getText(), tf_user_email.getText(), tf_user_sdt.getText(), ta_DiaChi.getText(), image, true);
				System.out.println(user.toString());
				Service.getInstance().registerInfo(user.toJsonObject("registerInfo"));
				dialog.dispose();
			}
		});
		bt_save.setFont(new Font("Segoe UI Black", Font.BOLD, 27));
		bt_save.setBounds(596, 487, 167, 51);
		add(bt_save);
		
		this.setBackground(new Color(200, 195, 236));
	}
	

	public JTextField getTf_user_TenTaiKhoan() {
		return tf_user_TenTaiKhoan;
	}

	public JTextField getTf_user_HoVaTen() {
		return tf_user_HoVaTen;
	}

	public JTextField getTf_user_email() {
		return tf_user_email;
	}

	public JTextField getTf_user_sdt() {
		return tf_user_sdt;
	}

	public JButton getBt_chonAnh() {
		return bt_chonAnh;
	}
}
