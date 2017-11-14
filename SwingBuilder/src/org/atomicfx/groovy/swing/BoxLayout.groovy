import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import javax.swing.border.LineBorder
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.BoxLayout as BXL
import java.awt.*

int numPanels = 20
def lineBorder = new LineBorder(Color.GRAY,1,false)
def defaultInsets = [0, 0, 10, 0]


swing = new SwingBuilder()
frame = swing.frame(id:'mainFrame', title:'CardLayout Demo',pack:true, visible:false, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
	panel(id:'comboPane',constraints:BorderLayout.NORTH) {
		boxLayout()
		vbox {
			hbox {
				comboBox(id:'cb', items:[
					'Card with JButtons',
					'Card with JTextField',
					'Card with JSlider'
				])
			}

			hbox {
				panel(constraints:'Card with JButtons') {
					button('Button 1')
					button('Button 2')
					button('Button 3')
				}
			}
			hbox {
				panel(constraints:'Card with JTextField') { textField(columns:20) }
				panel(constraints:'Card with JSlider') { slider() }
			}


			hbox {
				panel(constraints:'Card with JTextField') { textField(columns:20) }
				panel(constraints:'Card with JSlider') { slider() }
			}
		}
	}
}

frame.setMinimumSize(new Dimension(400,300));
frame.setMaximumSize(new Dimension(400,300));
frame.setResizable(false);
frame.setLocationRelativeTo(null);
//frame.getRootPane().setDefaultButton(saveButton);
frame.pack();
frame.setVisible(true);