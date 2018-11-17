package juniter.service.bma.model;


import java.io.Serializable;

public class UnitDTO implements Serializable {
    private static final long serialVersionUID = -6400555666088830671L;
    private final Integer size;
    private final Integer free;


    public UnitDTO(Integer size, Integer free){
        this.size = size ;
        this.free = free;
    }

    public Integer getFree() {
        return free;
    }

    public Integer getSize() {
        return size;
    }
}