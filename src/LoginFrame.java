import javax.swing.*;
import java.awt.event.*;
public class LoginFrame extends JFrame {


    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;


    public LoginFrame() {


        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    new MainFrame(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials");
                }
            }
        });


        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
    }


    private boolean validateLogin(String username, String password) {
        if (username.equals("user1") && password.equals("password1")) {
            return true;
        } else if (username.equals("user2") && password.equals("password2")) {
            return true;
        } else if (username.equals("user3") && password.equals("password3")) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }

}
