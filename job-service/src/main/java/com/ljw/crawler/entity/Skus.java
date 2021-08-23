package com.ljw.crawler.entity;


import java.io.Serializable;
import java.sql.Timestamp;

public class Skus implements Serializable {

  private Integer id;
  private String name;
  private Integer platformId;
  private Integer hotelId;
  private String url;
  private String originalUrl;
  private String score;
  private Integer maxPage;
  private Integer historyMaxPage;
  private Timestamp lastCheckTime;
  private Integer scoreFetch;
  private String enabled;
  private String fetched;
  private Timestamp addTime;
  private Timestamp updateTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPlatformId() {
    return platformId;
  }

  public void setPlatformId(Integer platformId) {
    this.platformId = platformId;
  }

  public Integer getHotelId() {
    return hotelId;
  }

  public void setHotelId(Integer hotelId) {
    this.hotelId = hotelId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public Integer getMaxPage() {
    return maxPage;
  }

  public void setMaxPage(Integer maxPage) {
    this.maxPage = maxPage;
  }

  public Integer getHistoryMaxPage() {
    return historyMaxPage;
  }

  public void setHistoryMaxPage(Integer historyMaxPage) {
    this.historyMaxPage = historyMaxPage;
  }

  public Timestamp getLastCheckTime() {
    return lastCheckTime;
  }

  public void setLastCheckTime(Timestamp lastCheckTime) {
    this.lastCheckTime = lastCheckTime;
  }

  public Integer getScoreFetch() {
    return scoreFetch;
  }

  public void setScoreFetch(Integer scoreFetch) {
    this.scoreFetch = scoreFetch;
  }

  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  public String getFetched() {
    return fetched;
  }

  public void setFetched(String fetched) {
    this.fetched = fetched;
  }

  public Timestamp getAddTime() {
    return addTime;
  }

  public void setAddTime(Timestamp addTime) {
    this.addTime = addTime;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }
}
