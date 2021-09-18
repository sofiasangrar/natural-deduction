package natded.problemDomain;

import java.io.IOException;

public interface IStorage {
    void updateSpaceData(NatDedSpace game) throws IOException;
    NatDedSpace getSpaceData() throws IOException;
}