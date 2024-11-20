package se.ki.education.nkcx.dto.util;


public interface DtoUtil<Entity, Req, Res> {
    Entity reqToEntity(Req req);

    Res entityToRes(Entity entity);

    Res prepRes(Entity entity);

    void setUpdatedValue(Req req, Entity entity);
}
