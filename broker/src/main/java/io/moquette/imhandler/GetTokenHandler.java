/*
 * This file is part of the Wildfire Chat package.
 * (c) Heavyrain2012 <heavyrain.lee@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package io.moquette.imhandler;

import cn.wildfirechat.proto.WFCMessage;
import io.moquette.persistence.MemorySessionStore;
import io.moquette.spi.impl.Qos1PublishHandler;
import io.moquette.spi.impl.security.TokenAuthenticator;
import io.netty.buffer.ByteBuf;
import win.liyufan.im.ErrorCode;
import win.liyufan.im.IMTopic;

@Handler(IMTopic.GetTokenTopic)
public class GetTokenHandler extends IMHandler<WFCMessage.GetTokenRequest> {
    @Override
    public ErrorCode action(ByteBuf ackPayload, String clientID, String fromUser, boolean isAdmin, WFCMessage.GetTokenRequest request, Qos1PublishHandler.IMCallback callback) {
        MemorySessionStore.Session session = m_sessionsStore.createUserSession(fromUser, clientID);
        TokenAuthenticator authenticator = new TokenAuthenticator();
        String strToken = authenticator.generateToken(fromUser);
        String result = strToken + "|" + session.getSecret() + "|" + session.getDbSecret();
        byte[] data = result.getBytes();
        ackPayload.ensureWritable(data.length).writeBytes(data);
        return ErrorCode.ERROR_CODE_SUCCESS;
    }
}
