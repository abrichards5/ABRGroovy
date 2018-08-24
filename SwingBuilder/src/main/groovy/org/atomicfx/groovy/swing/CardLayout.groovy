package org.atomicfx.groovy.swing;

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

def itemStateChanged = { evt ->
	cardLayout = (java.awt.CardLayout)swing.cards.getLayout()
	cardLayout.show(swing.cards, (String)swing.cb.getSelectedItem())
}
swing = new SwingBuilder()
frame = swing.frame(id:'mainFrame', title:'CardLayout Demo',pack:true, visible:false, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
	borderLayout()
	panel(id:'comboPane',constraints:BorderLayout.NORTH) {
		comboBox(id:'cb', items:[
			'Card with JButtons',
			'Card with JTextField',
			'Card with JSlider'
		], actionPerformed:itemStateChanged )
	}
	panel(id:'cards', constraints:BorderLayout.SOUTH) {
		cardLayout()
		panel(constraints:'Card with JButtons') {
			button('Button 1')
			button('Button 2')
			button('Button 3')
		}
		panel(constraints:'Card with JTextField') { textField(columns:20) }
		panel(constraints:'Card with JSlider') { slider() }
	}
}

frame.setMinimumSize(new Dimension(400,300));
frame.setMaximumSize(new Dimension(400,300));
frame.setResizable(false);
frame.setLocationRelativeTo(null);
//frame.getRootPane().setDefaultButton(saveButton);
frame.pack();
frame.setVisible(true);