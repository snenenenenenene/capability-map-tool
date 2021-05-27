import axios from "axios";
import React, { Component } from "react";
import toast from "react-hot-toast";
export default class StatusQuickAdd extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statuses: [],
      environments: [],
      environmentName: props.environmentName,
      validityPeriod: new Date(),
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleDateChange = this.handleDateChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("validityPeriod", this.state.validityPeriod);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/status/`, formData)
      .then((response) => {
        toast.success("Added Status");
        this.props.updateDate();
      })
      .catch((error) => {
        console.log(error);
        toast.error("Failed to Add Status");
      });
  };

  async componentDidMount() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => this.setState({ statuses: response.data }));
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
    console.log(this.state.validityPeriod);
  }

  statusListRows() {
    return this.state.statuses.map((status) => {
      console.log(status.statusId + " " + status.validityPeriod + " Days");
      return <option value={status.statusId}>{status.validityPeriod}</option>;
    });
  }

  render() {
    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <label htmlFor="validityPeriod">Validity Period</label>
          <input
            type="date"
            id="validityPeriod"
            name="validityPeriod"
            className="form-control"
            placeholder="End Date"
            value={this.state.validityPeriod}
            onChange={this.handleDateChange}
          />
          <br></br>
          <button
            className="btn btn-primary"
            type="button"
            onClick={this.handleSubmit}
          >
            Submit
          </button>
        </form>
      </div>
    );
  }
}
