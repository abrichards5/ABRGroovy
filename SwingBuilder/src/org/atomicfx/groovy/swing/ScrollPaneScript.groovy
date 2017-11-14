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
frame = swing.frame(id:'mainFrame',title:'Property Manager', pack:true, visible:false, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
	panel(id:'mainPanel'){
		borderLayout()

		panel(id:'northPanel' , constraints:BorderLayout.NORTH){
			panel() {
				label('Global Property Manager',)


				button('Load', actionPerformed:{ mainFrame.dispose() })
				button('Delete', actionPerformed:{ mainFrame.dispose() })
				button('Refresh', actionPerformed:{ mainFrame.dispose() })
			}
		}


		scrollPane(constraints:BorderLayout.CENTER,border:compoundBorder([lineBorder]), verticalScrollBarPolicy:JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ) {
			vbox {
				(1..numPanels).each { num ->
					def panelID = "panel$num"
					def pane = panel( alignmentX:0f, id:panelID, background:java.awt.Color.white ) {
						label('description')
						textField( id: "description$num", text:panelID, columns: 70 )
						button( id: "buttonpanel$num", text:panelID, actionPerformed:{
							swing."$panelID".background = java.awt.Color.RED
						} )
					}
				}
			}
		}


		panel(id:'southPanel' , constraints:BorderLayout.SOUTH){
			button('Exit', actionPerformed:{ mainFrame.dispose() })
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