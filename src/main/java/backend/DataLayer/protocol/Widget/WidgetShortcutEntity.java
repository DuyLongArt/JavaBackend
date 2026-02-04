package backend.DataLayer.protocol.Widget;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "widget_shortcut", schema = "widgets")
public class WidgetShortcutEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shortcut_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "shortcut_name")
    private String shortcutName;

    @Column(name = "shortcut_url", length = Integer.MAX_VALUE)
    private String shortcutUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "folder_id")
    private WidgetFolderEntity folder;

}