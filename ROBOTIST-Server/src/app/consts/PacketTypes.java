package app.consts;

/**
 *
 * @author dingl01
 */
public enum PacketTypes {

    // [NAME]               [Server Format]                     [Client Format]  
    // -------------------------------------------------------------------------
    // Login
    LOGIN_CREDENTIALS,  //  [OK/KO]                             [Username;Password]
    LOGIN_USER_FACE,    //                                      [Image]
    SIGN_IN,            //  [OK/KO]                             [Username;Password]
    SERVER_FULL,        //  [-]                         

    // Sequence
    SEQUENCE_LIST,      //  Name[SEQUENCE]|Name[SEQUENCE]|      [-]
    ADD_SEQUENCE,       //  [OK/KO]                             Name[Sequence]
    READ_SEQUENCE,      //  [-]                                 [SEQUENCE]
    CURRENT_ACTION,     //  [i]                                 [-]

    // Robot
    MOVE_ROBOT_BODY,    //                                      [LeftSpeed;RightSpeed]
    MOVE_ROBOT_HEAD,    //                                      [DOWN,NONE,UP]
    FREEZE_ROBOT,       //                                      [-]
    UNFREEZE_ROBOT,     //                                      [-]
    TAKE_PICTURE_ROBOT, //  [Image]                             [-]
    ROBOT_VIEW,         //  [Image]                     

    // Other
    PING,               //  [ms]                                [ms]
    FIND_SERVER_ADDRESS;//  []                                  []
    // -------------------------------------------------------------------------
}
