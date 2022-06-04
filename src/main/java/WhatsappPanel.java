import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WhatsappPanel extends JPanel {

    private ChromeDriver driver;
    private JLabel loginStatus;
    private JLabel messageStatus;
    private JLabel newMessage;
    private JTextField phoneLabel;
    private JTextField messageLabel;
    private JComboBox<String> areaCodePhone;

    public WhatsappPanel() {
        this.setLayout(null);
        this.setBounds(Constant.WINDOW_X, Constant.WINDOW_Y, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        this.setBackground(Color.CYAN);
        this.loginStatus = new JLabel("Login status: Offline");
        this.loginStatus.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/70,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/50,Constant.LABEL_WIDTH,Constant.LABEL_HEIGHT);
        this.loginStatus.setForeground(Color.WHITE);
        this.add(this.loginStatus);
        this.messageStatus = new JLabel("Message status: ");
        this.messageStatus.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/70,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/4,Constant.LABEL_WIDTH,Constant.LABEL_HEIGHT);
        this.messageStatus.setForeground(Color.WHITE);
        this.add(this.messageStatus);
        this.newMessage = new JLabel("Incoming message:");
        this.newMessage.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/70,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/2,Constant.LABEL_WIDTH,Constant.LABEL_HEIGHT);
        this.newMessage.setForeground(Color.WHITE);
        this.add(this.newMessage);
        this.phoneLabel = new JTextField("Enter phone - seven number only");
        this.phoneLabel.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/2+100,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/2,Constant.LABEL_WIDTH,Constant.LABEL_HEIGHT);
        this.phoneLabel.setBackground(Color.GREEN);
        this.add(this.phoneLabel);
        this.messageLabel = new JTextField("Enter message");
        this.messageLabel.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/2,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/4,Constant.LABEL_WIDTH*2-100,Constant.LABEL_HEIGHT);
        this.messageLabel.setBackground(Color.GREEN);
        this.add(this.messageLabel);
        this.areaCodePhone = new JComboBox<>(Constant.CODE_PHONE);
        this.areaCodePhone.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/2,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/2,Constant.LABEL_WIDTH/2,Constant.LABEL_HEIGHT);
        this.add(areaCodePhone);
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/2,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/50,Constant.BUTTON_WIDTH,Constant.BUTTON_HEIGHT);
        this.add(submitButton);
        submitButton.addActionListener((e) -> {
            getPhoneAndMessage();
            System.out.println("2");
        });
        JButton reportButton = new JButton("Report");
        reportButton.setBounds(Constant.WINDOW_X+Constant.WINDOW_WIDTH/2+200,Constant.WINDOW_Y+Constant.WINDOW_HEIGHT/50,Constant.BUTTON_WIDTH,Constant.BUTTON_HEIGHT);
        this.add(reportButton);
        reportButton.addActionListener((e) -> {
           getReport();
        });
        ImageIcon background = new ImageIcon("official-whatsapp-background1.jpg");
        JLabel backGround = new JLabel(background);
        backGround.setBounds(Constant.WINDOW_X, Constant.WINDOW_Y, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        this.add(backGround);
        this.setVisible(true);
    }

    public void getPhoneAndMessage(){
        String phone = "972"+this.areaCodePhone.getSelectedItem() + this.phoneLabel.getText();
        String message = this.messageLabel.getText();
        if (checkTextPhone(phone) && checkTextOfMessage(message)){
            login(phone,message);
        }
    }

    public boolean checkTextPhone(String phone){
        boolean ans = true;
        int index = 0;
        if (phone.length()!=12) {
            ans = false;
            this.messageStatus.setText("Illegal Number");
        }
        else {
            while (index < phone.length()) {
                if (Character.isLetter(phone.charAt(index))) {
                    ans = false;
                    this.messageStatus.setText("Illegal Number");
                    break;
                }
                index++;
            }
        }
        return ans;
    }

    public boolean checkTextOfMessage(String message){
        if (message.length()!=0){
            return true;
        }
        else {
            this.messageStatus.setText("Empty Message");
            return false;
        }
    }

    public void login (String phone,String message){
        System.setProperty("webdriver.chrome.driver","C:\\Users\\USER\\Documents\\chromedriver.exe");
        this.driver = new ChromeDriver();
            this.driver.get("https://web.whatsapp.com/send?phone="+phone);
            this.driver.manage().window().maximize();
        Thread t = new Thread(() -> {
            WebElement checkLogin =null;
            boolean success = false;
            while (success==false) {
                try {
                    checkLogin = this.driver.findElement(By.className("_2vbn4"));
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                if (checkLogin!=null) {
                    this.loginStatus.setText("Login status: Online");
                        success=true;
                        checkIfPhoneExist(message);

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void checkIfPhoneExist(String message){
        String checkIfPhoneExist = null;
        boolean phoneExist = true;
            try {
                checkIfPhoneExist = this.driver.findElement(By.className("_3J6wB")).getText(); // class of popUp:'invalid number'
            }catch (Exception e1){
                e1.printStackTrace();
            }
            while (checkIfPhoneExist!=null) {
                    if (checkIfPhoneExist.contains("invalid")) {
                        this.messageStatus.setText("Message status: Invalid Number ");
                        phoneExist = false;
                        this.driver.close();
                        break;
                    }
                    else {
                        checkIfPhoneExist = null;
                    }
                try {
                    checkIfPhoneExist = this.driver.findElement(By.className("_3J6wB")).getText(); // class of popUp:'invalid number'
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                }
            if (phoneExist){
                pastMessage(message);
            }

            }

    public void pastMessage (String message){
        this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            WebElement newMessage = this.driver.findElement(By.xpath("//div[@title=\"Type a message\"]"));
            newMessage.click();
            newMessage.clear();
            newMessage.sendKeys(message);
            this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            newMessage.sendKeys(Keys.ENTER);
            checkLastMessage();
    }

    public void checkMyMessageStatus(){
                try {
                    List<WebElement> messagesStatus = this.driver.findElements(By.xpath("//div[@class=\"do8e0lj9 l7jjieqr k6y3xtnu\"]"));
                    if (messagesStatus.get(messagesStatus.size()-1).findElement(By.xpath("//span[@aria-label=\" Sent \"]")).isDisplayed()){
                        this.messageStatus.setText("Message status: Sent");
                    }
                   if (messagesStatus.get(messagesStatus.size()-1).findElement(By.xpath("//span[@aria-label=\" Delivered \"]")).isDisplayed()){
                       this.messageStatus.setText("Message status: Delivered");
                   }
                    if (messagesStatus.get(messagesStatus.size()-1).findElement(By.xpath("//span[@aria-label=\" Read \"]")).isDisplayed()){
                        this.messageStatus.setText("Message status: Read");
                    }
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
    }

    public void checkLastMessage (){
        Thread t = new Thread(() -> {
            boolean newMassage = false;
            while (newMassage==false){
                checkMyMessageStatus();
                List<WebElement> messagesList = this.driver.findElements(By.xpath("//div[@class=\"_1Gy50\"]"));
                String lastMessage =messagesList.get(messagesList.size()-1).getText();
                if (!lastMessage.equals(this.messageLabel.getText())){
                    System.out.println("new message");
                    this.newMessage.setText("incoming message: " + lastMessage);
                    newMassage = true;
                    this.driver.close();
                }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
        t.start();

    }

    public void getReport(){
        String report = "";
        if (this.messageStatus.getText()=="Message status: Invalid Number "){
            report = "Invalid Number";
        }
        else {
            report = " From: " + "972-"+this.areaCodePhone.getSelectedItem()+"-" + this.phoneLabel.getText() + "\n Message: " + this.messageLabel.getText() + "\n Comment: " + this.newMessage.getText();
        }

            writeToTextFile(report, Constant.FILE_ADDRESS);
            System.out.println(readFromTextFile(Constant.FILE_ADDRESS));
    }

    public static void writeToTextFile(String report,String path){
        try {
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            writer.writeObject(report);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static String readFromTextFile (String path){
        String report = "";
        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream reader = new ObjectInputStream(file);
            report = (String) reader.readObject();
            reader.close();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return report;

    }


}

