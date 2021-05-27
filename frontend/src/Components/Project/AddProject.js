import React, { Component } from "react";

export default class AddProject extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit = (e) => {
    e.preventDefault();
    let environmentName = this.environmentname.value;
    let path = `environment/${environmentName}`;
    // this is the part !!!
    this.props.history.push(path);
  };

  componentDidMount() {}

  render() {
    return (
      <div class="jumbotron">
        <h1>Add Environment</h1>
        <div class="row">
          <div class="col-sm-6">
            <div>
              <p>Recent Environments</p>
              <table className=" table table-striped">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Description</th>
                  </tr>
                </thead>
                <tbody>{this.recentEnvironmentTableRow()}</tbody>
              </table>
            </div>
          </div>
          <div class="col-sm-6">
            <p>New Environments</p>
            <form
              className="form-inline"
              onSubmit={this.handleSubmit}
              method="POST"
            >
              <input
                type="text"
                className="form-control"
                placeholder="New Environment"
                ref={(input) => (this.environmentname = input)}
              ></input>
              <button className="btn primary" type="submit">
                Add
              </button>
            </form>
          </div>
        </div>
      </div>
    );
  }
}
