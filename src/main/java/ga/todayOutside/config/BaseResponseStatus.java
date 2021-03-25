package ga.todayOutside.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),
    SUCCESS_POST_ADDRESS(true, 1200, "주소 등록에 성공하였습니다."),
    SUCCESS_DELETE_ADDRESS(true, 1201, "주소 삭제를 성공하였습니다."),
    SUCCESS_READ_ADDRESS(true,1202,"주소 목록 조회에 성공하였습니다."),
    SUCCESS_READ_NOW_WEATHER(true,1203,"현재 날씨 조회에 성공하였습니다."),
    SUCCESS_READ_TODAY_LOW_HIGH(true,1204,"오늘의 최저기온과 최고기온 조회에 성공하였습니다."),
    SUCCESS_READ_TIME_WEATHER(true,1205,"시간별 날씨예보 조회에 성공하였습니다."),
    SUCCESS_READ_WEEKLY_HIGH_LOW_VALUE(true,1206,"주간 최고 최저 기온조회에 성공하였습니다."),
    SUCCESS_READ_WEEKLY_RAIN_WEATHER(true,1207,"주간 강수확률과 날씨예보 조회를 성공하였습니다."),
    SUCCESS_PATCH_ADDRESS_NAME(true,1208,"회원주소 수정에 성공하였습니다."),
    SUCCESS_PATCH_ADDRESS_ORDER(true,1209,"회원주소 순서 수정에 성공하였습니다."),
    SUCCESS_READ_THIRD_ADDRESS_NAME(true,1210,"동 정보 전체 정보 조회에 성공하였습니다."),
    SUCCESS_POST_THIRD_ADDRESS_NAME(true,1211,"동 정보 등록에 성공하였습니다."),
    SUCCESS_POST_MESSAGE_BOARD(true,1212,"게시판 글 정보 등록에 성공하였습니다."),
    SUCCESS_PATCH_MESSAGE_BOARD(true,1213,"게시판 글 정보 수정에 성공하였습니다."),
    SUCCESS_DELETE_MESSAGE_BOARD(true,1214,"게시판 글 정보 삭제를 성공하였습니다."),
    SUCCESS_READ_MESSAGE_BOARD_HEART(true,1215,"데일리하트 순위로 게시글을 조회하는데 성공하였습니다."),
    SUCCESS_READ_MESSAGE_BOARD_RECENTLY(true,1216,"최신 순으로 게시글을 조회하는데 성공하였습니다."),
    SUCCESS_READ_MESSAGE_BOARD(true,1217,"게시글 조회애 성공하였습니다."),
    SUCCESS_READ_COMMENT_LIST(true,1218,"게시글에 해당하는 댓글 리스트 조회에 성공하였습니다."),
    SUCCESS_POST_HEART(true,1219,"성공적으로 하트를 눌렀습니다."),
    SUCCESS_READ_DUST(true,1220,"미세먼지 조회에 성공하였습니다."),
    SUCCESS_POST_COMMENTS(true, 1300, "댓글 등록이 성공하였습니다."),
    SUCCESS_GET_COMMENTS(true, 1301, "댓글 조회가 성공하였습니다."),
    SUCCESS_PATCH_COMMENTS(true, 1302, "댓글 수정이 성공하였습니다."),
    SUCCESS_GET_DISASTER(true, 1303, "재난정보 조회가 성공하였습니다."),
    SUCCESS_DELETE_COMMENTS(true, 1304, "댓글 삭제가 성공하였습니다."),




    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_USERID(false, 2001, "유저 아이디 값을 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "유효하지 않은 JWT입니다."),
    EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
    EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
    WRONG_PASSWORD(false, 2032, "비밀번호를 다시 입력해주세요."),
    DO_NOT_MATCH_PASSWORD(false, 2033, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
    EMPTY_NICKNAME(false, 2040, "닉네임을 입력해주세요."),
    EMPTY_ADDRESS_ORDER(false, 2201, "변경하고 싶은 주소 순서를 입력해주세요."),
    INVALID_ADDRESS_ORDER(false, 2202, "변경하고 싶은 순서 값을 확인해주세요."),
    EMPTY_MESSAGE_BOARD(false, 2203, "게시글을 작성해주세요."),
    EMPTY_MESSAGE_COMMENTS(false, 2300, "댓글을 작성해주세요"),
    EMPTY_DATE(false, 2301, "월, 일을 확인해주세요"),
    INVALID_ACCESSTOKEN(false, 2302, "유효하지 않은 accessToken 입니다."),

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),
    FAILED_TO_POST_ADDRESS(false, 3200, "최대 주소 등록 개수를 초과했습니다"),
    NOT_FOUND_ADDRESS(false, 3201, "존재하지 않는 주소 인덱스 입니다."),
    FAILED_TO_GET_ADDRESS(false, 3202, "주소 리스트 조회에 실패하였습니다."),
    HAVE_NOT_ADDRESS(false, 3203, "유저가 갖고 있지 않은 주소 인덱스입니다."),
    EMPTY_THIRD_ADDRESS(false, 3204, "동 정보가 비어있습니다."),
    NOT_FOUND_MESSAGE_BOARD(false, 3205, "존재하지 않는 게시글 입니다."),
    NOT_MATCH_USER_MESSAGE_BOARD(false, 3206, "해당 유저가 작성한 게시글이 아닙니다."),
    NOU_FOUND_COMMENT(false, 3207, "게시글에 해당하는 댓글이 없습니다."),
    FAILED_TO_POST_COMMENTS(false, 3300, "댓글 등록이 실패했습니다."),
    FAILED_TO_GET_COMMENTS(false, 3301, "댓글 조회가 실패했습니다."),
    FAILED_TO_PATCH_COMMENTS(false, 3302, "댓글 수정이 실패했습니다."),
    FAILED_TO_GET_DISASTER(false, 3303, "재난 조회가 실패했습니다."),
    FAILED_TO_DELETE_COMMENTS(false, 3304, "댓글 삭제가 실패했습니다."),
    NOT_FOUND_MESSAGE_BY_USERS(false, 3305, "유저가 작성한 게시글이 없습니다."),


    // 4000 : Database 오류
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
