/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sms.app.framework.communication.localayer.cmd;

/**
 *
 * @author Administrator
 */
public class CRC {
    
    
 static int  crc16(byte[] msg , int n)
{
    int i = 0,crc = 0xffff,j=0;

    while(n-- != 0)
    {
        crc ^= msg[j];
        for(i = 0;i < 8;i++)
        {
               if((crc&0x01)!=0)
               {
                   crc = (crc >> 1) ^ 0x8408;
               }else crc >>= 1;
        }
        j++;
    }
    return (~crc)&0xffff;
}
    public static  int crc8(byte[] ptr, long len)
    {  
        char crc,i;
        crc = 0;
        for(int j = 0 ; j < len ; j++)
        {
           crc = (char)((ptr[j]^crc)&0x00ff);
           for(i = 0;i < 8;i++)
           {
               if((crc&0x01)>0)
               {
                   crc = (char)(((crc >> 1) ^ 0x8C)&0x00ff);
               }else crc >>= 1;
           }
        }
        return crc&0x00ff;
    }
    
    
    
    public static void main(String[] arg)
    {
        String str = "khakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksdkhakfhkhkahskjgfgs64564564565ajkdgsadfgsfdgsfsfggdfsdfjgsfgsjdgfsdgfsgdfsgdfkjdlfjlksjdflksdjflskdjfsdklfjsdlkfjslkdjfslkdjflksd";
    
        System.out.println("crc="+CRC.crc16(str.getBytes(), str.getBytes().length));
    }
}
