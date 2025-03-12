package com.camelcase.taskapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TaskUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-03-12T12:51:37.560464508Z[Etc/UTC]", comments = "Generator version: 7.13.0-SNAPSHOT")
public class TaskUpdateRequest {

  private @Nullable String title;

  private @Nullable String description;

  /**
   * Gets or Sets taskStatus
   */
  public enum TaskStatusEnum {
    PENDING("pending"),
    
    IN_PROGRESS("in_progress"),
    
    COMPLETED("completed");

    private String value;

    TaskStatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TaskStatusEnum fromValue(String value) {
      for (TaskStatusEnum b : TaskStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TaskStatusEnum taskStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate dueDate;

  public TaskUpdateRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Title must be between 3 and 100 characters.
   * @return title
   */
  @Size(min = 3, max = 100) 
  @Schema(name = "title", description = "Title must be between 3 and 100 characters.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public TaskUpdateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskUpdateRequest taskStatus(TaskStatusEnum taskStatus) {
    this.taskStatus = taskStatus;
    return this;
  }

  /**
   * Get taskStatus
   * @return taskStatus
   */
  
  @Schema(name = "taskStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("taskStatus")
  public TaskStatusEnum getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskStatusEnum taskStatus) {
    this.taskStatus = taskStatus;
  }

  public TaskUpdateRequest dueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Due date must be a valid date in the format YYYY-MM-DD.
   * @return dueDate
   */
  @Valid 
  @Schema(name = "dueDate", description = "Due date must be a valid date in the format YYYY-MM-DD.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dueDate")
  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskUpdateRequest taskUpdateRequest = (TaskUpdateRequest) o;
    return Objects.equals(this.title, taskUpdateRequest.title) &&
        Objects.equals(this.description, taskUpdateRequest.description) &&
        Objects.equals(this.taskStatus, taskUpdateRequest.taskStatus) &&
        Objects.equals(this.dueDate, taskUpdateRequest.dueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, taskStatus, dueDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskUpdateRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    taskStatus: ").append(toIndentedString(taskStatus)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

