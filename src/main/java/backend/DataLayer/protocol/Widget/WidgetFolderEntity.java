package backend.DataLayer.protocol.Widget;

import backend.DataLayer.protocol.Account.AccountEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "widget_folder", schema = "widgets")
public class WidgetFolderEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "folder_name")
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "identity_id")
    private AccountEntity identity;

    @OneToMany(mappedBy = "folder")
    private Set<WidgetShortcutEntity> widgetShortcuts = new LinkedHashSet<>();

}