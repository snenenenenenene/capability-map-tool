import React, { Component } from "react";
import toast from "react-hot-toast";
import API from "../../Services/API";
export default class StatusQuickAdd extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),

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
    this.state.api.createEntity({ name: "status" });
    const formData = new FormData();
    formData.append("validityPeriod", this.state.validityPeriod);
    await this.state.api.endpoints.status
      .create(formData)
      .then((response) => {
        toast.success("Added Status");
        this.props.updateDate();
      })
      .catch((error) => {
        toast.error("Failed to Add Status");
      });
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
  }
  render() {
    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <label htmlFor='validityPeriod'>Validity Period</label>
          <input
            type='date'
            id='validityPeriod'
            name='validityPeriod'
            className='form-control'
            placeholder='End Date'
            value={this.state.validityPeriod}
            onChange={this.handleDateChange}
          />
          <br></br>
          <button
            className='btn btn-primary'
            type='button'
            onClick={this.handleSubmit}
          >
            Submit
          </button>
        </form>
      </div>
    );
  }
}
