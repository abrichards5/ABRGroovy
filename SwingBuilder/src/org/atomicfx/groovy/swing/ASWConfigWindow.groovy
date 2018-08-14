package org.atomicfx.groovy.swing;

import java.awt.*
import java.awt.event.*

import javax.swing.JFrame
import javax.swing.WindowConstants as WC
import javax.swing.border.LineBorder
import javax.swing.event.*

import org.atomicfx.groovy.swing.demo.MyTableModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import groovy.swing.factory.LineBorderFactory


class aModel {
	@Bindable String env
	@Bindable String envPath
	@Bindable String username
	@Bindable String password
}

def log = LoggerFactory.getLogger(MyTableModel.class.getName());
log.info("begin");
def envOptions=['MIE', 'SP11CT', 'SP11TEST']
def envPathOptions=['PROXY', 'DIRECT']

def model = new aModel();
//initialize
model.setEnv('SP11TEST')
model.setEnvPath('DIRECT')
model.setUsername('username')
model.setPassword('pass')

def frameDim = [300, 150] as Dimension;
def defaultInsets = [0, 0, 10, 0]
// used for visual debug.  set size to 0 to hide
def lineBorder = new LineBorder(Color.GRAY,1,false)

SwingBuilder swing = new SwingBuilder()
JFrame frame = swing.build() {
	frame(id:'mainFrame'
	, title:'Configuration'
	, size:frameDim
	, visible:false
	, pack:true
	, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
		// set layout for this frame
		borderLayout(vgap: 5,hgap:5)
		panel(constraints:BorderLayout.CENTER
		,border:compoundBorder([lineBorder, emptyBorder(5)])
		,size:[300, 100]) {
			panel(border:compoundBorder([lineBorder, emptyBorder(5)])) {
				// set layout for this panel
				gridBagLayout()

				row = 0
				label('Environment: ',constraints: gbc(gridx:0,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))
				env=comboBox(id: 'env', items: envOptions,
				selectedItem: model.getEnv()
				,constraints: gbc(gridx:1,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))

				row++
				label('Serivce Path: ',constraints: gbc(gridx:0,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))
				envPath=comboBox(id: 'env', items: envPathOptions,
				selectedItem: model.getEnvPath()
				,constraints: gbc(gridx:1,gridy:row,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets))

				row++
				panel(constraints: gbc(gridx:0,gridy:row,gridwidth:GridBagConstraints.REMAINDER,fill:GridBagConstraints.HORIZONTAL,insets:defaultInsets),

				) {
					gridLayout(columns:2, rows:3)
					label(text:'Username')
					username = textField(model.getUsername(),id:'usernameTextField', columns:15)

					label(text:'Password')
					passwordTextField= passwordField(model.getPassword(),id: 'passwordTextField', columns:15)

					label(text:'Show Password')
					checkBox(id: 'showPasswordCheckbox')
				}
			}
		}
		// button panel
		panel(constraints:BorderLayout.SOUTH,border:compoundBorder([lineBorder])) {
			saveButton = button(defaultButton:false, text:'Save',
			actionPerformed: { logModel(model) })
			exitButton = button(defaultButton:true, text:'Exit',
			actionPerformed: { mainFrame.dispose() })
		}

		bind(source:usernameTextField, sourceProperty:'text', target:model, targetProperty:'username')
		bind(source:passwordTextField, sourceProperty:'text', target:model, targetProperty:'password')
		bind(source:env, sourceProperty:'selectedItem', target:model, targetProperty:'env')
		bind(source:envPath, sourceProperty:'selectedItem', target:model, targetProperty:'envPath')

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
	println(model.getEnv());
	println(model.getEnvPath());
	println(model.getUsername());
	println(model.getPassword());
}