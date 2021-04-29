import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import plusImg from '../img/plus.png'



export default class Environment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            environmentname: props.environmentname,
            capabilities: 0,
            itApplications: 0,
            programs: 0,
            strategies: 0,
            strategyItems: 0,
            projects: 0,
            resources: 0,
            businessProcesses: 0,
            status: 0
        };

    }

    addStatus = () => {
        return '1'
    }

    componentDidMount() {
    }

    render() {
        const environmentName = this.props.match.params.name;
        return(
            <div>
                <h4>Home &gt; Environment &gt; {environmentName}</h4>
                <div className="container jumbotron">
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Capabilities</h5>
                                <div className="text-center">
                                <Link to={`${environmentName}/add/capability`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.capabilities} capabilities</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategies</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/strategy`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.strategies} strategies</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Resources</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/resources`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.resources} resources</small>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">IT-Applications</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/ITapplication`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.itApplications} IT-applications</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategy Items</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/strategyItem`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.strategyItems} stategy items</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Business processes</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/businessProcess`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.businessProcesses} business processes</small>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Programs</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/program`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.programs} programs</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Projects</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/project`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.projects} projects</small>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Status</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/add/status`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <small className="text-muted">{this.state.status} statuses</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    }








}