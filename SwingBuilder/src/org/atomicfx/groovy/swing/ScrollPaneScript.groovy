import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.BoxLayout as BXL

int numPanels = 20

swing = new SwingBuilder()
frame = swing.frame(id:'mainFrame',title:'test', pack:true, visible:true, defaultCloseOperation:WC.HIDE_ON_CLOSE) {
	panel(id:'mainPanel'){
		scrollPane( verticalScrollBarPolicy:JScrollPane.VERTICAL_SCROLLBAR_ALWAYS ) {
			vbox {
				(1..numPanels).each { num ->
					def panelID = "panel$num"
					def pane = panel( alignmentX:0f, id:panelID, background:java.awt.Color.GREEN ) {
						label('description')
						textField( id: "description$num", text:panelID, columns: 70 )
						button( id: "buttonpanel$num", text:panelID, actionPerformed:{
							swing."$panelID".background = java.awt.Color.RED
						} )
					}
				}
			}
		}

		boxLayout(axis: BXL.Y_AXIS)
		panel(id:'secondPanel' , alignmentX: 0f){
			button('Quit', actionPerformed:{
				//frame.visible = false
				//dispose of frame will terminate application
				mainFrame.dispose()
			})
		}
	}
}
frame.size = [frame.width, 600]