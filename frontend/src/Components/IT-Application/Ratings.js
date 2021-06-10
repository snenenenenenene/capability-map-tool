import React, { Component } from "react";
import ReactStars from "react-stars";

export default class AddITApplication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      currentScalability: props.currentScalability,
      expectedScalability: props.expectedScalability,
      currentPerformance: props.currentPerformance,
      expectedPerformance: props.expectedPerformance,
      currentSecurityLevel: props.currentSecurityLevel,
      expectedSecurityLevel: props.expectedSecurityLevel,
      currentStability: props.currentStability,
      expectedStability: props.expectedStability,
      currentValue: props.currentValue,
      showModal: false,
    };
    this.handleInputChange = this.handleInputChange.bind(this);
  }
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <div className="row">
            <div className="col-sm">
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="currentScalability: ">
                    Current Scalability
                  </label>
                  <ReactStars
                    name="currentScalability"
                    id="currentScalability"
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ currentScalability: newValue });
                      this.props.transferRatings(
                        "currentScalability",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.currentScalability}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="expectedScalability">
                    Expected Scalability
                  </label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ expectedScalability: newValue });
                      this.props.transferRatings(
                        "expectedScalability",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.expectedScalability}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="currentPerformance">
                    Current Performance
                  </label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ currentPerformance: newValue });
                      this.props.transferRatings(
                        "currentPerformance",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.currentPerformance}
                  />
                </div>
              </div>
            </div>
            <div className="col-sm">
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="expectedPerformance">
                    Expected Performance
                  </label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ expectedPerformance: newValue });
                      this.props.transferRatings(
                        "expectedPerformance",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.expectedPerformance}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="currentSecurityLevel">
                    Current Security Level
                  </label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ currentSecurityLevel: newValue });
                      this.props.transferRatings(
                        "currentSecurityLevel",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.currentSecurityLevel}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="expectedSecurityLevel">
                    Expected Security Level
                  </label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ expectedSecurityLevel: newValue });
                      this.props.transferRatings(
                        "expectedSecurityLevel",
                        newValue
                      );
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.expectedSecurityLevel}
                  />
                </div>
              </div>
            </div>
            <div className="col-sm">
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="currentStability">Current Stability</label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ currentStability: newValue });
                      this.props.transferRatings("currentStability", newValue);
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.currentStability}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="expectedStability">Expected Stability</label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ expectedStability: newValue });
                      this.props.transferRatings("expectedStability", newValue);
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.expectedStability}
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group col-md">
                  <label htmlFor="currentValue">Current Value</label>
                  <ReactStars
                    count={5}
                    onChange={(newValue) => {
                      this.setState({ currentValue: newValue });
                      this.props.transferRatings("currentValue", newValue);
                    }}
                    size={24}
                    half={false}
                    color2={"#ffd700"}
                    value={this.state.currentValue}
                  />
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    );
  }
}
