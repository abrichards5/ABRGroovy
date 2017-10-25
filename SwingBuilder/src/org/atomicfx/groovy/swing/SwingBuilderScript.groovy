import groovy.swing.SwingBuilder
import java.awt.event.*
import javax.swing.event.*
import javax.swing.JFrame
import javax.swing.WindowConstants as WC
import java.awt.*

def frameDim = [300,150] as Dimension;
SwingBuilder swing = new SwingBuilder()
JFrame frame = swing.build() {
    frame(id:'mainFrame'
		, title:'Swing Listener example'
		, size:frameDim
		, visible:false
		, pack:true
		, defaultCloseOperation:WC.DISPOSE_ON_CLOSE) {
		borderLayout(vgap: 20,hgap:20)
		
		panel(constraints:BorderLayout.CENTER,border:compoundBorder([emptyBorder(20), titledBorder('Title:')])
			,size:[300,100]) {
			panel(constraints:BorderLayout.CENTER,border:emptyBorder(10)) {
        gridLayout(cols: 2, rows: 0)
        label 'Input text: '
        input = textField(columns:10, actionPerformed: { echo.text = input.text.toUpperCase() })        
        label 'Echo: '
        echo = label()
			}
		}
		
		
		
        input.document.addDocumentListener(
            [insertUpdate: { echo.text = input.text },
             removeUpdate: { echo.text = input.text },
             changedUpdate: { e -> println e }] as DocumentListener)

        input.addFocusListener(
            [focusGained: { e -> println "Focus gained: $e.cause"},
             focusLost: {e -> println "Focus lost: $e.cause"}] as FocusListener)

        input.addCaretListener({ e ->  println "Caret event: $e"})
    }
}

frame.setMinimumSize(new Dimension(400,400));
frame.setMaximumSize(new Dimension(400,400));
frame.setResizable(false);
frame.setLocationRelativeTo(null);
frame.setVisible(true);