#!/usr/bin/env groovy
package org.atomicfx.groovy.swing

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import javax.swing.JFrame;


class Model {
	@Bindable String select
	@Bindable String text
}


def model = new ComboBox.Model();

def frame  = new SwingBuilder().frame(pack: true, show: false, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
	panel {
		vbox {
			comboBox(id: 'cb', items: ['enable', 'disable'],
			selectedItem: bind(target: model, targetProperty: 'select')
			)
			textField(columns:10, text: bind(target: model, targetProperty: 'text'),
			enabled: bind(source: cb, sourceEvent: 'itemStateChanged', sourceValue: { model.select == 'enable' })
			)
		}
	}
}
// show window
frame.setVisible(true);
