package com.wistron.occ.parser;

import com.wistron.occ.enums.InfoCode;
import com.wistron.occ.enums.Type;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParserImpl implements Parser {

    private List<byte[]> massages;

    @Override
    public void process(InputStream in) throws IOException {
        byte[] buffer = new byte[1];
        List<String> perMsgList = new ArrayList<>();

        int msgListIndex = 0;
        Type msgTitle = Type.NULL;
        int totalLength = 0;
        List<String> infoList = new ArrayList<>();
        Map<InfoCode, List<String>> infoMap = new HashMap<>();

        while (in.read(buffer) > -1) {



            perMsgList.add(Hex.encodeHexString(buffer));


            //	第二個byte可決定傳過來的訊息類型 msgTitle
            if (perMsgList.size() > 1 && msgTitle == Type.NULL) {
                if (StringUtils.equals(perMsgList.get(msgListIndex - 1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "dd")) {
                    System.out.println(Type.ACK);
                    msgTitle = Type.ACK;
                } else if (StringUtils.equals(perMsgList.get(msgListIndex - 1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "ee")) {
                    System.out.println(Type.NACK);
                    msgTitle = Type.NACK;
                } else if (StringUtils.equals(perMsgList.get(msgListIndex - 1), "aa") && StringUtils.equals(perMsgList.get(msgListIndex), "bb")) {
                    System.out.println(Type.MSG);
                    msgTitle = Type.MSG;
                } else {
                    perMsgList.clear();
                    msgListIndex = 0;
                    msgTitle = Type.NULL;
                    totalLength = 0;
                    infoMap.clear();
                    infoList.clear();
                }
            }

            //	第六、七個byte會知道總長度
            if (perMsgList.size() == 7) {
                String length = perMsgList.get(5) + perMsgList.get(6);
                totalLength = Integer.parseInt(length, 16);
            }
//            System.out.println(msgTitle + "=> perMsgList.size():" + perMsgList.size() + ". totalLength:" + totalLength);

            //	擷取完整byte字串後，解析字串
            if (perMsgList.size() == totalLength) {
                switch (msgTitle) {
                    case MSG:    //	取INFO 訊息欄位，從第8位 取到 倒數第4位
                        System.out.println("MSG");
                        composeInfoMap(perMsgList, totalLength, infoList, infoMap);
                        converterInfoMsg(infoMap);
                        break;
                    case ACK:    //	寫入訊息成功
                        System.out.println("ACK");
                        break;
                    case NACK:    //	寫入訊息失敗
                        System.out.println("NACK");
                        break;
                    default:
                        break;
                }

                System.out.println("list2 clear");

                // save db
                perMsgList.clear();
                msgListIndex = 0;
                msgTitle = Type.NULL;
                totalLength = 0;
                infoMap.clear();
                infoList.clear();
            } else {
                msgListIndex++;
            }

        }
    }


    private void composeInfoMap(List<String> perMsgList, int totalLength,
                                List<String> infoList, Map<InfoCode, List<String>> infoMap) {
        StringBuffer buf = new StringBuffer();
        for (int j = 8; j <= totalLength - 3; j++) {
            System.out.print(perMsgList.get(j - 1) + " ");
            if (j == 8 || j == 9) {
                buf.append(perMsgList.get(j - 1));
            } else {
                infoList.add(perMsgList.get(j - 1));
            }
        }
        infoMap.put(InfoCode.lookup(buf.toString()), infoList);
    }

    private void converterInfoMsg(final Map<InfoCode, List<String>> infoMap) {
        // 只會有一筆
        for (InfoCode infoCode : infoMap.keySet()) {
            System.out.println(infoCode + ":" + infoMap.get(infoCode));
            switch (infoCode) {
                case CODE_5F0F:
//                    converter5F0F(infoMap.get(infoCode));
                    break;

                default:
                    System.out.println("default");
                    break;
            }

        }
    }

    /**
     * 1. 用於控制中心監視號誌控制器之簡單燈態運作狀況用。
     * 2. 主要給無線通訊環境使用，以顯示紅黃綠燈。
     * <p>
     * SignalMap
     * GreenSignalMap
     * YellowSignalMap
     * RedSignalMap
     * <p>
     * Bit 設為 1 表示該方向有燈，Bit 設為 0 表示該方向沒有燈
     * Bit 0:北向 Bit 1: 東北向 Bit 2: 東向 Bit 3: 東南向
     * Bit 4:南向 Bit 5: 西南向 Bit 6: 西向 Bit 7: 西北向
     */
//    private void converter5F0F(List<String> paramList) {
//        for (String param : paramList) {
//            byte byteParam = (byte) Integer.parseInt(param.substring(0, 2), 16);
//            for (int i = 0; i < 8; i++) {
//                int aa = getBit(byteParam, i);
//                System.out.print(aa + ". ");
//            }
//            System.out.println("");
//        }
//    }
//
//    private int getBit(byte id, int position) {
//        return (id >> position) & 1;
//    }
}
