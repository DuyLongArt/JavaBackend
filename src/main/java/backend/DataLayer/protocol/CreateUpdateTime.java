package backend.DataLayer.protocol;

import java.time.LocalDateTime;

public interface CreateUpdateTime
{
    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    void setCreateTime(LocalDateTime createTime);
    void setUpdateTime(LocalDateTime updateTime);
}
