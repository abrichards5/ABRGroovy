import groovy.swing.SwingBuilder
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import javax.swing.WindowConstants as WC
import java.awt.Insets

class User {
	String username
	String password
}

def user = new User()
user.setUsername('gg')
user.setPassword('PASS')

def s = new SwingBuilder()
def d = s.dialog(id:'credentialsDialog', modal:true, title:'Enter Login Credentials', pack:true, show:true) {
	panel(layout:new GridBagLayout()) {
		def ins = new Insets(10,10,0,10)
		label(text:'Username',  constraints:gbc(gridx:0,gridy:0,gridwidth:2,insets:ins))
		textField(user.getUsername(), id:'usernameTextField', columns:15,  constraints:gbc(gridx:2,gridy:0,gridwidth:4,insets:ins))
		label(text:'Password',  constraints:gbc(gridx:0,gridy:1,gridwidth:2,insets:ins))
		passwordField(user.getPassword(),id: 'passwordTextField', columns:15, constraints:gbc(gridx:2,gridy:1,gridwidth:4,insets:ins))
		button(defaultButton:true, text:'Login', constraints:gbc(gridx:0,gridy:2,gridwidth:GridBagConstraints.REMAINDER,insets:[10, 10, 10, 10]),
		actionPerformed: { credentialsDialog.dispose() })
	}

	bind(source:usernameTextField, sourceProperty:'text', target:user, targetProperty:'username')
	bind(source:passwordTextField, sourceProperty:'text', target:user, targetProperty:'password')
}
s.bind().rebind()
System.out.println("username/password = " + user.username + "/" + user.password);