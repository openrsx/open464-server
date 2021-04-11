package com.paragon464.gameserver.io.database.table.log;

import com.paragon464.gameserver.io.database.pool.impl.ConnectionPool;
import com.paragon464.gameserver.model.entity.mob.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class CommandTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTable.class);

    public static void save(final Player player, final String message) {
       /* ConnectionPool.execute(() -> {
            try (Connection connection = ConnectionPool.getPool().getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT into paragon_player_log_command (user_id, command, date)VALUES (?,?,?)")) {
                statement.setInt(1, player.getDetails().getUserId());
                statement.setString(2, message);
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (SQLException e) {
                while (e != null) {
                    LOGGER.error("An error occurred whilst saving command log: [player={}, command={}]!", player.getDetails(), message, e);
                    e = e.getNextException();
                }
            }
        });*/
    }
}
