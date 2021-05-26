import React, { Component } from "react";

export default class NotFound extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  componentDidMount() {}

  render() {
    return (
      <div className="text-center">
        <h1>Not Found Error</h1>
        <p className="jumbotron">Environment Does not exist</p>
      </div>
    );
  }
}
