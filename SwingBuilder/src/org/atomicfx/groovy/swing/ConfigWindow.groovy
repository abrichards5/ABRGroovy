import java.awt.*
import java.awt.event.*

import javax.swing.JFrame
import javax.swing.WindowConstants as WC
import javax.swing.border.LineBorder
import javax.swing.event.*

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import groovy.swing.factory.LineBorderFactory


class aModel {
	@Bindable String select
	@Bindable String text
	@Bindable String username
	@Bindable String password
	@Bindable String env
}

def envOptions=['MIE', 'SP11CT','SP11TEST']

def model = new aModel();
//initialize
model.setEnv('SP11CT')
model.setUsername('username')

def frameDim = [300, 150] as Dimension;
def lineBorder = new LineBorder(Color.RED)
SwingBuilder swing = new SwingBuilder()
JFrame frame = swing.build() {
	frame(id:'mainFrame'
	, title:'Swing Listener example'
	, size:frameDim
	, visible:false
	, pack:true
	, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
		borderLayout(vgap: 20,hgap:20)

		panel(constraints:BorderLayout.CENTER
			,border:compoundBorder([
			 lineBorder
			,emptyBorder(20)
			,titledBorder('Title:')
				])
			,borderLayout(vgap: 20,hgap:20)		
			,size:[300, 100]) {
				panel(constraints:BorderLayout.CENTER
					,border:compoundBorder([
						lineBorder,
						,emptyBorder(10)
						])
				) {
				gridLayout(cols: 2, rows: 6)
				label 'Input text: '
				input = textField(columns:10, actionPerformed: {
					echo.text = input.text.toUpperCase()
				})
				label 'Echo: '
				echo = label()

				label 'Environment: '
				env = comboBox(id: 'env', items: envOptions,
				selectedItem: bind(target: model, targetProperty: 'env'))
			}
			
			
			
			panel(layout:new GridBagLayout()) {
				def ins = new Insets(10,10,0,10)
				label(text:'Username',  constraints:gbc(gridx:0,gridy:0,gridwidth:2,insets:ins))
				textField(id:'usernameTextField', columns:15,  constraints:gbc(gridx:2,gridy:0,gridwidth:4,insets:ins))
				label(text:'Password',  constraints:gbc(gridx:0,gridy:1,gridwidth:2,insets:ins))
				passwordField(id: 'passwordTextField', columns:15, constraints:gbc(gridx:2,gridy:1,gridwidth:4,insets:ins))
			}
			button(defaultButton:true, text:'Save',
			actionPerformed: { logModel(model) })
			button(defaultButton:true, text:'Exit',
			actionPerformed: { mainFrame.dispose() })

			bind(source:usernameTextField, sourceProperty:'text', target:model, targetProperty:'username')
			bind(source:passwordTextField, sourceProperty:'text', target:model, targetProperty:'password')
		}




		input.document.addDocumentListener(
				[insertUpdate: {
						echo.text = input.text
					},
					removeUpdate: {
						echo.text = input.text
					},
					changedUpdate: { e -> println e }] as DocumentListener)

		input.addFocusListener(
				[focusGained: { e -> println "Focus gained: $e.cause" },
					focusLost: { e -> println "Focus lost: $e.cause" }] as FocusListener)

		input.addCaretListener({ e ->  println "Caret event: $e" })
	}
}

frame.setMinimumSize(new Dimension(400,400));
frame.setMaximumSize(new Dimension(400,400));
frame.setResizable(false);
frame.setLocationRelativeTo(null);
frame.setVisible(true);

def logModel(aModel model) {
	System.out.println(model.getEnv());
	System.out.println(model.getUsername());
	System.out.println(model.getPassword());
}