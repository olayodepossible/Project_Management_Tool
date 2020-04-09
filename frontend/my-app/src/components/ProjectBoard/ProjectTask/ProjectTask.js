import React, { Component } from "react";

class ProjectTask extends Component {
  render() {
    const { project_TaskProp2 } = this.props;

    return (
      <div className="card mb-1 bg-light">
        <div className="card-header text-primary">
          ID: {project_TaskProp2.projectSequence} -- Priority:{" "}
          {project_TaskProp2.priorityString}
        </div>
        <div className="card-body bg-light">
          <h5 className="card-title">{project_TaskProp2.summary}</h5>
          <p className="card-text text-truncate ">
            {project_TaskProp2.acceptanceCriteria}
          </p>
          <a href="" className="btn btn-primary">
            View / Update
          </a>

          <button className="btn btn-danger ml-4">Delete</button>
        </div>
      </div>
    );
  }
}

export default ProjectTask;
