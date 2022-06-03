import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.*;
import java.util.List;
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
        this.setBounds(0, 0, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        this.loginStatus = new JLabel("login status\n");
        this.loginStatus.setBounds(80,10,200,100);
        this.loginStatus.setBackground(Color.WHITE);
        this.add(this.loginStatus);
        this.messageStatus = new JLabel("message status\n");
        this.messageStatus.setBounds(300,10,100,100);
        this.messageStatus.setBackground(Color.WHITE);
        this.add(this.messageStatus);
        this.newMessage = new JLabel("incoming message:\n");
        this.newMessage.setBounds(550,10,100,100);
        this.newMessage.setBackground(Color.WHITE);
        this.add(this.newMessage);
        this.phoneLabel = new JTextField("enter phone\n");
        this.phoneLabel.setBounds(50,150,100,100);
        this.phoneLabel.setBackground(Color.GREEN);
        this.messageLabel = new JTextField("enter message\n");
        this.messageLabel.setBounds(160,150,100,100);
        this.messageLabel.setBackground(Color.GREEN);
        this.add(this.phoneLabel);
        this.add(this.messageLabel);
        this.areaCodePhone = new JComboBox<>(Constant.CODE_PHONE);
        this.areaCodePhone.setBounds(10,150,30,50);
        this.add(areaCodePhone);
        JButton button = new JButton("submit");
        button.setBounds(80, 300, 100, 50);
        this.add(button);
        button.addActionListener((e) -> {
            getPhoneAndMessage();
        });
        this.setVisible(true);
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
                    this.loginStatus.setText("login");
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
                        this.messageStatus.setText("invalid number");
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
    if (phone.length()!=12)
        ans = false;
    else {
        while (index < phone.length()) {
            if (Character.isLetter(phone.charAt(index))) {
                ans = false;
                break;
            }
            index++;
        }
    }
    return ans;
    }

    public boolean checkTextOfMessage(String message){
        if (message!=null){
            return true;
        }
        else return false;
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
                        this.messageStatus.setText("Sent");
                    }
                   if (messagesStatus.get(messagesStatus.size()-1).findElement(By.xpath("//span[@aria-label=\" Delivered \"]")).isDisplayed()){
                       this.messageStatus.setText("Delivered");
                   }
                    if (messagesStatus.get(messagesStatus.size()-1).findElement(By.xpath("//span[@aria-label=\" Read \"]")).isDisplayed()){
                        this.messageStatus.setText("Read");
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


}

