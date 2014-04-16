package com.liliya.quiz.model;

import com.liliya.quiz.server.QuizServer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.rmi.RemoteException;

public class Serializer implements Serializable {

    private static final String FILENAME = "server_state.xml";

    public void encodeData(QuizServer serverState) {

        XMLEncoder encode = null;
        try {
            setUpFile();
            encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("." + File.separator + FILENAME)));
            encode.writeObject(serverState);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            if (encode != null) {
                encode.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public QuizServer decodeData() throws RemoteException {
        QuizServer serverState = null;
        XMLDecoder decode = null;
        if (!getSerializationFile().exists() || getSerializationFile().length() == 0) {
            return new QuizServer();
        }

        try {
            decode = new XMLDecoder(new BufferedInputStream(new FileInputStream("." + File.separator + FILENAME)));
            serverState = (QuizServer) decode.readObject();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Shouldn't happen", ex);
        } finally {
            if (decode != null) {
                decode.close();
            }
        }
        return serverState;
    }

    private void setUpFile() {

        try {
            getSerializationFile().createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException("Issue creating file", ex);
        }
    }

    private File getSerializationFile() {
        return new File("." + File.separator + FILENAME);

    }
}

