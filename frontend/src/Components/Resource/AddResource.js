import React, { Component } from "react";

export default class AddResource extends Component {
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
    this.props.history.push(path);
  };

  componentDidMount() {}

  render() {
    return <p>hi</p>;
  }
}
