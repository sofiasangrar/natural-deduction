package natded.persistence;

import natded.problemDomain.IStorage;
import natded.problemDomain.NatDedSpace;

import java.io.*;

public class LocalStorageImpl implements IStorage {

    private static File GAME_DATA = new File(
            System.getProperty("user.home"),
            "gamedata.txt"
    );

    @Override
    public void updateSpaceData(NatDedSpace space) throws IOException {
        try {


            FileOutputStream fileOutputStream =
                    new FileOutputStream(GAME_DATA);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(space);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new IOException("Unable to access Space Data");
        }
    }

    @Override
    public NatDedSpace getSpaceData() throws IOException {

        FileInputStream fileInputStream =
                new FileInputStream(GAME_DATA);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try {
            NatDedSpace spaceState = (NatDedSpace) objectInputStream.readObject();
            objectInputStream.close();
            return spaceState;
        } catch (ClassNotFoundException e) {
            throw new IOException("File Not Found");
        }
    }}
