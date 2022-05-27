import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class WhatsappPanel extends JPanel {

    private ChromeDriver driver;
    private JLabel statusLabel;
    private JTextField phoneLabel;
    private JTextField messageLabel;



    public WhatsappPanel() {
        this.setLayout(null);
        this.setBounds(0, 0, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        this.statusLabel = new JLabel("user interface\n");
        this.statusLabel.setBounds(80,10,200,100);
        this.statusLabel.setBackground(Color.WHITE);
        this.add(this.statusLabel);
        this.phoneLabel = new JTextField("enter phone\n");
        this.phoneLabel.setBounds(10,150,100,100);
        this.phoneLabel.setBackground(Color.GREEN);
        this.messageLabel = new JTextField("enter message\n");
        this.messageLabel.setBounds(120,150,100,100);
        this.messageLabel.setBackground(Color.GREEN);
        this.add(this.phoneLabel);
        this.add(this.messageLabel);
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
                    success=true;
                    this.statusLabel.setText("login");
                    pastMessage(message);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void getPhoneAndMessage(){
            String phone = this.phoneLabel.getText();
            String message = this.messageLabel.getText();
            if (checkPhone(phone)&&checkMessage(message)){
                login(phone,message);
            }
        }

    public boolean checkPhone (String phone){
    boolean ans = true;
    int index = 0;
    while (index<phone.length()){
        if (Character.isLetter(phone.charAt(index))){
            ans = false;
            break;
        }
        index++;
    }
    return ans;
    }

    public boolean checkMessage (String message){
        if (message!=null){
            return true;
        }
        else return false;

    }

    public boolean pastMessage (String message){
       WebElement newMessage =this.driver.findElement(By.xpath("//div[@title=\"Type a message\"]"));
       newMessage.click();
       newMessage.clear();
       newMessage.sendKeys(message);
       this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
       newMessage.sendKeys(Keys.ENTER);
       return true;
    }
}

