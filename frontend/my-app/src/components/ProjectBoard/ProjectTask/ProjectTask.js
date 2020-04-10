import React, { Component } from "react";
import { Link } from "react-router-dom";

class ProjectTask extends Component {
  render() {
    const { project_TaskProp2 } = this.props;

    let priorityString;
    let priorityClass;

    if (project_TaskProp2.priority === 1) {
      priorityClass = "bg-danger text-light";
      priorityString = "HIGH";
    }
    if (project_TaskProp2.priority === 2) {
      priorityClass = "bg-warning text-light";
      priorityString = "MEDIUM";
    }
    if (project_TaskProp2.priority === 3) {
      priorityClass = "bg-info text-light";
      priorityString = "LOW";
    }

    return (
      <div className="card mb-1 bg-light">
        <div className={`card-header text-primary ${priorityClass}`}>
          ID: {project_TaskProp2.projectSequence} -- Priority: {priorityString}
        </div>
        <div className="card-body bg-light">
          <h5 className="card-title">{project_TaskProp2.summary}</h5>
          <p className="card-text text-truncate ">
            {project_TaskProp2.acceptanceCriteria}
          </p>
          <Link
            to={`/updateProjecttask/${project_TaskProp2.projectIdentifier}/${project_TaskProp2.projectSequence}`}
            className="btn btn-primary"
          >
            View / Update
          </Link>

          <button className="btn btn-danger ml-4">Delete</button>
        </div>
      </div>
    );
  }
}

export default ProjectTask;
