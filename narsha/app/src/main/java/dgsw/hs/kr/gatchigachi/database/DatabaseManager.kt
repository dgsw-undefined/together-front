package dgsw.hs.kr.gatchigachi.database

/**
 * @author dawncrow
 */

internal class DatabaseManager {

    val CreateTableAlert =
            "CREATE TABLE IF NOT EXISTS alert (" +
                    "id bigint(20)," +
                    "user_id bigint(20) NULL," +
                    "docs text NOT NULL," +
                    "type int(11) NOT NULL," +
                    "kind int(11) NOT NULL," +
                    "recevied_date text," +
                    "read_date datetime NOT NULL," +
                    "sender bigint(20) NULL," +
                    "receiver bigint(20) NULL," +
                    "team_id bigint(20) NULL);"

    val CreateTableTeam =
            "CREATE TABLE IF NOT EXISTS `team` (" +
                    "`id` bigint(20)," +
                    "`name` varchar(255) NOT NULL," +
                    "`subject` varchar(255) NOT NULL," +
                    "`area` varchar(255) NOT NULL," +
                    "`docs` text NOT NULL," +
                    "`leader_id` bigint(20) NOT NULL," +
                    "`profile` varchar(255) NULL, "+
                    "`member_limit` int(11) DEFAULT NULL," +
                    "`member_count` int(11) DEFAULT '1'," +
                    "`is_my_team` int(11) NOT NULL,"+
                    "primary key(id)" +
                    ");"

    val CreateTableTeamMember =
            "CREATE TABLE IF NOT EXISTS `team_member` (" +
                    "`id` bigint(20) ," +
                    "`team_id` bigint(20) NOT NULL," +
                    "`user_idx` bigint(20) NOT NULL," +
                    "`name` varchar(255) NOT NULL,"+
                    "`field` varchar(255) DEFAULT NULL," +
                    "`enroll_date` varchar(255) DEFAULT NULL,"+
                    "`inviter_id` bigint(20) NOT NULL," +
                    "`is_leader` int(11) NOT NULL DEFAULT '0'," +
                    "primary key(id));"

    val CreateTableTruster =
            "CREATE TABLE IF NOT EXISTS `truster` (" +
                    "    `id` bigint(20) ," +
                    "    `user_id` bigint(20) NOT NULL," +
                    "    `truster_id` bigint(20) NOT NULL," +
                    "    `truster_date` datetime DEFAULT CURRENT_TIMESTAMP" +
                    "    );"

    val CreateTableUser =
            "CREATE TABLE IF NOT EXISTS `user` (" +
                    "    `idx` bigint(20)," +
                    "    `id` varchar(255) NOT NULL," +
                    "    `name` varchar(255) NOT NULL," +
                    "    `pw` varchar(255) NOT NULL," +
                    "    `email` varchar(255) NOT NULL," +
                    "    `interested` varchar(255) NOT NULL," +
                    "    `github` varchar(255) NOT NULL," +
                    "    `profile` varchar(255) NULL," +
                    "    `tec` varchar(255) NULL,"+
                    "    `field` varchar(255) NOT NULL," +
                    "    `position` varchar(255) NOT NULL," +
                    "    `phone` varchar(255) NOT NULL," +
                    "    `is_me` int(11)," +
                    "    primary key(idx));"

    val CreateTableToken =
            "CREATE TABLE IF NOT EXISTS `token` ("+
            "    `idx` int(11) NOT NULL," +
            "    `token` varchar(255) NOT NULL," +
            "    primary key(idx));"

}
