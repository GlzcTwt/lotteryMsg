/**	
 * Copyright (c) 2010-2014  Seaway I.T. Co, Ltd, All Rights Reserved.
 *		This source code contains CONFIDENTIAL INFORMATION and 
 *		TRADE SECRETS of Seaway I.T, Co, Ltd. ANY REPRODUCTION,
 * 		DISCLOSURE OR USE IN WHOLE OR IN PART is EXPRESSLY PROHIBITED, 
 * 		except as may be specifically authorized by prior written 
 *		agreement or permission by Seaway I.T, Co, Ltd.
 *
 *		THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE, The copyright 
 *		notice above does not evidence any actual or intended publication
 *		of such source code. 
 *
 * 项目名称(ProjectName)：mobilebank_mtp
 * 文件名称(FileName):ISendMessageService.java
 * @author：fanghua
 * @e_mail：fanghua@seaway.net.cn
 * @date：2014年5月14日上午10:35:30
 * --------------------修改历史-----------------------
 * Reporting Bugs:
 * Modification history:
 */
package com.lottery.print.service;

import com.lottery.print.model.JycMsgLog;

/**
 * 
 * @ClassName: ISendMessageService.java
 * @Description: 统一发送消息
 * @author fanghua
 * @version V1.0
 * @Date 2014年5月14日 上午10:36:40
 */
public interface ISendMessageService {

	public boolean sendMessage(JycMsgLog tbTradeRecords) throws Exception;

}
