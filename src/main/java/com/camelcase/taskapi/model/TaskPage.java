package com.camelcase.taskapi.model;

import java.net.URI;
import java.util.Objects;
import com.camelcase.taskapi.model.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TaskPage
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-03-10T18:22:10.574140745Z[Etc/UTC]", comments = "Generator version: 7.13.0-SNAPSHOT")
public class TaskPage {

  @Valid
  private List<@Valid Task> tasks = new ArrayList<>();

  private @Nullable Integer totalPages;

  private @Nullable Integer totalItems;

  public TaskPage tasks(List<@Valid Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  public TaskPage addTasksItem(Task tasksItem) {
    if (this.tasks == null) {
      this.tasks = new ArrayList<>();
    }
    this.tasks.add(tasksItem);
    return this;
  }

  /**
   * Get tasks
   * @return tasks
   */
  @Valid 
  @Schema(name = "tasks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tasks")
  public List<@Valid Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<@Valid Task> tasks) {
    this.tasks = tasks;
  }

  public TaskPage totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
   */
  
  @Schema(name = "totalPages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public TaskPage totalItems(Integer totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
   */
  
  @Schema(name = "totalItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalItems")
  public Integer getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskPage taskPage = (TaskPage) o;
    return Objects.equals(this.tasks, taskPage.tasks) &&
        Objects.equals(this.totalPages, taskPage.totalPages) &&
        Objects.equals(this.totalItems, taskPage.totalItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tasks, totalPages, totalItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskPage {\n");
    sb.append("    tasks: ").append(toIndentedString(tasks)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
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

