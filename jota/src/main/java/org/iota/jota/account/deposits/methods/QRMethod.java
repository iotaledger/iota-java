package org.iota.jota.account.deposits.methods;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import org.bouncycastle.util.encoders.Base64;
import org.iota.jota.account.deposits.DepositConditions;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.glxn.qrgen.javase.QRCode;

public class QRMethod implements DepositMethod<QRCode>{

    public QRMethod() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public DepositConditions parse(QRCode method) {
        ByteArrayOutputStream baos = method.stream();
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream( baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        String conditions = null;
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            conditions = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        System.out.println(conditions);
        byte b[] = Base64.decode(conditions.getBytes()); 
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si;
        try {
            si = new ObjectInputStream(bi);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        DepositConditions depositConditions;
        try {
            depositConditions = (DepositConditions) si.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return depositConditions;
    }

    @Override
    public QRCode build(DepositConditions conditions) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = null;
        try {
            so = new ObjectOutputStream(bo);
            so.writeObject(conditions);
            so.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return QRCode.from(new String(Base64.encode(bo.toByteArray())));
    }

}
