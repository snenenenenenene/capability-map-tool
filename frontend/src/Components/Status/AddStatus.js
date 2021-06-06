import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";
export default class AddStatus extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      statuses: [],
      environments: [],
      environmentName: this.props.match.params.name,
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
    await this.state.api.endpoints.status
      .create(formData)
      .then((response) => toast.success("Added Status"))
      .catch((error) => {
        toast.error("Failed to Add Status");
      });
    this.props.history.push(
      `/environment/${this.state.environmentName}/status`
    );
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "status" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.status
      .getAll()
      .then((response) => this.setState({ statuses: response.data }));
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item active' aria-current='page'>
              Add Status
            </li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>Add Status</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
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
                  </div>
                </div>
              </div>
              <div className='col-sm-6'></div>
            </div>
            <button
              className='btn btn-primary'
              type='button'
              onClick={this.handleSubmit}
            >
              Submit
            </button>
          </form>
        </div>
        <div></div>
      </div>
    );
  }
}
