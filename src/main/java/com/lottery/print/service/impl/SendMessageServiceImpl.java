/**
 * Copyright (c) 2010-2014  Seaway I.T. Co, Ltd, All Rights Reserved.
 * This source code contains CONFIDENTIAL INFORMATION and
 * TRADE SECRETS of Seaway I.T, Co, Ltd. ANY REPRODUCTION,
 * DISCLOSURE OR USE IN WHOLE OR IN PART is EXPRESSLY PROHIBITED,
 * except as may be specifically authorized by prior written
 * agreement or permission by Seaway I.T, Co, Ltd.
 * <p>
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE, The copyright
 * notice above does not evidence any actual or intended publication
 * of such source code.
 * <p>
 * 项目名称(ProjectName)：mobilebank_mtp
 * 文件名称(FileName):SendMessageServiceImpl.java
 *
 * @author：fanghua
 * @e_mail：fanghua@seaway.net.cn
 * @date：2014年5月14日上午10:37:14 --------------------修改历史-----------------------
 * Reporting Bugs:
 * Modification history:
 */
package com.lottery.print.service.impl;

import com.lottery.print.model.JycMsgLog;
import com.lottery.print.service.IJycMsgLogService;
import com.lottery.print.service.ISendEmailService;
import com.lottery.print.service.ISendMessageService;
import com.lottery.print.service.ISendSmsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @ClassName: SendMessageServiceImpl.java
 * @Description: 发送消息
 * @author fanghua
 * @version V1.0
 * @Date 2014年5月14日 上午10:37:48
 */
@Service
public class SendMessageServiceImpl implements ISendMessageService {
    protected final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private IJycMsgLogService tradeRecordsService;
    @Autowired
    private ISendEmailService sendEmailService;
    @Autowired
    private ISendSmsService sendSmsService;

    @SuppressWarnings("rawtypes")
    public boolean sendMessage(JycMsgLog tbTradeRecords) {
        Map map = new HashMap();
        boolean retState = true;
        try {
//			// 修改记录表加锁
//			tbTradeRecords.setTradeState(2);
//			tradeRecordsService.updateTbTradeRecordsLockState(tbTradeRecords);
//			logger.info("发送消息记录加锁成功|" + tbTradeRecords.getId() + ",toName|"
//					+ tbTradeRecords.getMessNumber());

            if (tbTradeRecords.getMessType() == 1 || tbTradeRecords.getMessType() == 2) {
                // 短信接口1、验证码 2、营销短信
                logger.info("调用发送短信消息开始|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber() + ",messType=" + tbTradeRecords.getMessType());
                map = sendSmsService.SendSms(tbTradeRecords.getMessNumber(),
                        tbTradeRecords.getMessTitle(),
                        tbTradeRecords.getMessContent(),
                        tbTradeRecords.getMessType());

                logger.info("调用发送短信消息结束|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber() + ",map|"
                        + map);
            } else if (tbTradeRecords.getMessType() == 3) {
                // 邮件
                logger.info("调用发送邮件消息开始|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber());
                map = sendEmailService.SendEmail(
                        tbTradeRecords.getMessNumber(),
                        tbTradeRecords.getMessTitle(),
                        tbTradeRecords.getMessContent());

                logger.info("调用发送邮件消息结束|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber() + ",map|"
                        + map);
            } else {
                // 语音验证码
                logger.info("调用发送语音消息开始|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber());
                map = sendSmsService.SendVoice(tbTradeRecords.getMessNumber(),
                        tbTradeRecords.getMessTitle(),
                        tbTradeRecords.getMessContent(),
                        tbTradeRecords.getMerchName());

                logger.info("调用发送语音消息结束|" + tbTradeRecords.getId()
                        + ",toName|" + tbTradeRecords.getMessNumber() + ",map|"
                        + map);
            }

            logger.info("发送消息修改记录开始|" + tbTradeRecords.getId()
                    + ",toName|" + tbTradeRecords.getMessNumber() + ",map|"
                    + map);

            // 修改当前发送状态
            if (map.get("merchId") != null && !"".equals(map.get("merchId"))) {
                tbTradeRecords.setMerchId(Long.valueOf(map.get("merchId").toString()));
            }
            tbTradeRecords.setTimeSend(new Date());
            tbTradeRecords.setCallState(20);
            tbTradeRecords.setTradeState(new Integer(map.get("state")
                    .toString()));
            tbTradeRecords.setExtOrderId(map.get("extOrderId") != null ? map.get("extOrderId").toString() : null);
            tradeRecordsService.updateTbTradeRecordsSendState(tbTradeRecords);
            logger.info("发送消息修改记录完成|" + tbTradeRecords.getId()
                    + ",toName|" + tbTradeRecords.getMessNumber() + ",map|"
                    + map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("发送消息异常|" + tbTradeRecords.getId() + ",toName|"
                    + tbTradeRecords.getMessNumber() + ",异常消息|"
                    + e.getMessage());
            retState = false;

        }

        return retState;
    }

}
