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


// set to 0 to disable debug borders.
def debugBorderWidth=1;

def envOptions=['MIE', 'SP11CT', 'SP11TEST']

def model = new aModel();
//initialize
model.setEnv('SP11CT')
model.setUsername('username')
model.setPassword('pass')

def frameDim = [300, 150] as Dimension;
def defaultInsets = [0, 0, 10, 0]
// used for visual debug.  set size to 0 to hide
def lineBorder = new LineBorder(Color.RED,debugBorderWidth,false)

SwingBuilder swing = new SwingBuilder()
JFrame frame = swing.build() {
	frame(id:'mainFrame'
	, title:'Swing Listener example'
	, size:frameDim
	, visible:false
	, pack:true
	, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
		// set layout for this frame
		borderLayout(vgap: 5,hgap:5)

		panel(constraints:BorderLayout.CENTER
		,border:compoundBorder([
			lineBorder
			,
			emptyBorder(20)
			,
			titledBorder('Title:')
		])
		,size:[300, 100]) {
			panel(
					border:compoundBorder([
						lineBorder,
						,
						emptyBorder(10)
					])
					) {
						// set layout for this panel
						// warning gridBagLayout does NOT support nesting
						gridBagLayout()

						row = 0

						label('Input text: ',constraints: gbc(gridx:0,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets) )

						input = textField(columns:10,
						,constraints: gbc(gridx:1,gridy:row
						//,gridwidth:REMAINDER
						//,anchor:WEST
						,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets) ,
						actionPerformed: {
							echo.text = input.text.toUpperCase()
						})

						row++

						label('Echo: ',constraints: gbc(gridx:0,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets) )

						echo = label(constraints: gbc(gridx:1,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))

						row++

						label('Environment: ',constraints: gbc(gridx:0,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))
						env=comboBox(id: 'env', items: envOptions,
						//selectedItem: bind(target: model, targetProperty: 'env')
						selectedItem: 'SP11CT'
						,constraints: gbc(gridx:1,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))
						
						
						
						row++
						panel(constraints: gbc(gridx:0,gridy:row,gridwidth:GridBagConstraints.REMAINDER,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets),
						,border:compoundBorder([
							lineBorder
							,
							emptyBorder(20)
						])
						) {
							gridLayout(columns:2, rows:3)
							label(text:'Username')
							textField(model.getUsername(),id:'usernameTextField', columns:15
								//,text: bind(target: model, targetProperty: 'username')
								)

							label(text:'Password')
							//passwordTextField= textField(model.getPassword(),id: 'passwordTextField', columns:15)
							passwordTextField= passwordField(model.getPassword(),id: 'passwordTextField', columns:15)
							label(text:'Show Password')
							checkBox(id: 'showPasswordCheckbox')
						}
					}
		}
		// button panel
		panel(constraints:BorderLayout.SOUTH
		,border:compoundBorder([
			lineBorder
			,
			emptyBorder(10)
			,
			titledBorder('buttons:')
		])
		) {
			saveButton = button(defaultButton:false, text:'Save',
			actionPerformed: { logModel(model) })
			exitButton = button(defaultButton:true, text:'Exit',
			actionPerformed: { mainFrame.dispose() })
		}

		bind(source:usernameTextField, sourceProperty:'text', target:model, targetProperty:'username')
		bind(source:passwordTextField, sourceProperty:'text', target:model, targetProperty:'password')
		bind(source:env, sourceProperty:'selectedItem', target:model, targetProperty:'env')

		// examples of registered listeners
		showPasswordCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					passwordTextField.setEchoChar((char) 0);
				} else {
					passwordTextField.setEchoChar((char)'*');
				}
			}
		});
		
		
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
//frame.getRootPane().setDefaultButton(saveButton);
frame.pack();
frame.setVisible(true);



def logModel(aModel model) {
	System.out.println(model.getEnv());
	System.out.println(model.getUsername());
	System.out.println(model.getPassword());
}