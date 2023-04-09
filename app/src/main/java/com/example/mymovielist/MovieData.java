package com.example.mymovielist;

public class MovieData {
    String rank;
    String movieNm;
    String openDt;

    String movieCd;

    public MovieData(String rank, String movieNm, String openDt, String movieCd) {
        this.rank = rank;
        this.movieNm = movieNm;
        this.openDt = openDt;
        this.movieCd = movieCd;
    }
//    public MovieData(String rank, String movieNm, String openDt) {
//        this.rank = rank;
//        this.movieNm = movieNm;
//        this.openDt = openDt;
//    }
//    public MovieData() {
//        this.rank = rank;
//        this.movieNm = movieNm;
//        this.openDt = openDt;
//    }

    public String getMovieNm() { return movieNm; }
    public String getRank() { return rank; }
    public String getOpenDt() { return openDt; }
    public String getMovieCd() { return movieCd; }
    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }
    public void setRank() {
        this.rank = rank;
    }
    public void setOpenDt() {
        this.openDt = openDt;
    }
    public void setMovieCd() { this.movieCd = movieCd; }
}
