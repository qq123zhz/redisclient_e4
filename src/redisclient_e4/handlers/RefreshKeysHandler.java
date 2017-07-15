/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package redisclient_e4.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import redisclient_e4.models.DbElement;
import redisclient_e4.parts.RedisKeyListPart;
import redisclient_e4.utils.PlatFormUtils;
/**
 * @author Administrator
 *
 */
public class RefreshKeysHandler extends AbstractRedisHandler {
	
	public static final String CMDID = "redisclient_e4.command.refreshkeys";
	MPart activePart;
	@Execute
	public void execute(Shell shell,@Active MPart activePart){
		super.execute(shell);
		this.activePart = activePart;
		RedisKeyListPart part = (RedisKeyListPart) activePart.getObject(); 
		
		
		DbElement dbElement = part.getElement();
		Map paramMap = new HashMap<>();
		paramMap.put("DbElement", dbElement.toString());
		PlatFormUtils.executeCommand(commandService, handlerService, OpenDbHandler.CMDID, paramMap);
		 
		
	}
	
	 
}
