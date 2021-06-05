import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class EditProgram extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),

      environments: [],
      statuses: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      programId: this.props.match.params.id,
      programName: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("programName", this.state.programName);
    await this.state.api.endpoints.program
      .update(formData, this.state.programId)
      .then((response) => toast.success("Program Added Successfully!"))
      .catch((error) => toast.error("Could not Add Program"));
    this.props.history.push(
      `/environment/${this.state.environmentName}/program`
    );
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "program" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.program
      .getOne({ id: this.state.programId })
      .then((response) =>
        this.setState({ programName: response.data.programName })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });
  }
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='shadow breadcrumb'>
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
              {this.state.programId}
            </li>
          </ol>
        </nav>
        <div className='jumbotron shadow'>
          <h3>Edit Program</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm'>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='programName'>Name Program</label>
                    <input
                      type='text'
                      id='programName'
                      name='programName'
                      className='form-control'
                      placeholder='Name Program'
                      value={this.state.programName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='technology'>Technology</label>
                    <input
                      type='text'
                      id='technology'
                      name='technology'
                      className='form-control'
                      placeholder='Technology'
                      value={this.state.technology}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='version'>Version</label>
                    <input
                      type='text'
                      id='version'
                      name='version'
                      className='form-control'
                      placeholder='Version'
                      value={this.state.version}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <button
                      className='btn btn-secondary btn-block'
                      style={{ marginTop: 32 }}
                      type='button'
                      onClick={this.handleSubmit}
                    >
                      Submit
                    </button>
                  </div>
                </div>
              </div>
              <div className='col-sm'></div>
              <div className='col-sm'></div>
            </div>
          </form>
        </div>
      </div>
    );
  }
}
